package io.github.jahrim.chess.game.service.components.ports

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.chess.util.scala.id.Id as LegacyId
import io.github.jahrim.chess.game.service.components.data.codecs.vertx.Codecs as VertxCodecs
import io.github.jahrim.chess.game.service.components.events.{
  ChessGameServiceEvent,
  Event,
  GameOverUpdateEvent
}
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameAlreadyStartedException,
  GameIdAlreadyTakenException,
  GameNotFoundException,
  NoAvailableGamesException
}
import io.github.jahrim.chess.game.service.components.ports.ChessGameModel.*
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort.{ChessGameMap, Id}
import io.github.jahrim.chess.game.service.components.ports.model.game.ChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameOver.ResultMap
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice,
  ServerSituation,
  ServerState
}
import io.github.jahrim.chess.game.service.components.proxies.statistics.StatisticsServiceProxy
import io.github.jahrim.chess.game.service.util.activity.{ActivityLogging, LoggingFunction}
import io.github.jahrim.chess.game.service.util.vertx.FutureExtension.*
import io.github.jahrim.hexarc.architecture.vertx.core.components.PortContext
import io.vertx.core.{Future, Vertx}

import scala.reflect.ClassTag

/** Business logic of an [[ChessGamePort]]. */
class ChessGameModel(statisticsService: StatisticsServiceProxy = StatisticsServiceProxy.stub)
    extends ChessGamePort
    with ActivityLogging:
  /**
   * The [[Vertx]] instance where the asynchronous activities of this
   * service will be executed.
   */
  protected given Vertx = context.vertx

  private var _games: ChessGameMap = Map()
  private def games: ChessGameMap = this._games

  override protected def defaultActivityLogger: LoggingFunction = context.log.info(_)
  override protected def init(context: PortContext): Unit =
    activity("Register Vertx codecs")(VertxCodecs.registerInto(context.vertx))

  override def getGames: Future[ChessGameMap] =
    asyncActivity("Retrieve games") { this._games }

  override def createGame(gameConfiguration: GameConfiguration): Future[Id] =
    asyncActivityFlatten(s"Create game with id: ${gameConfiguration.gameIdOption}") {
      future {
        val gameId: Id = gameConfiguration.gameIdOption.getOrElse(LegacyId())
        val configuration: GameConfiguration =
          GameConfiguration.default
            .setGameId(gameId)
            .setIsPrivate(gameConfiguration.isPrivate)
            .setTimeConstraint(gameConfiguration.timeConstraint)
            .setGameMode(gameConfiguration.gameMode)
        games.get(gameId) match
          case Some(game) => throw GameIdAlreadyTakenException(gameId)
          case None       => (ChessGameServer(this.context.vertx), configuration)
      }.compose((game, configuration) =>
        game
          .configure(configuration)
          .compose(_ =>
            game.subscribe[GameOverUpdateEvent](event =>
              sendResults(game.id, event.payload.results).compose(_ => deleteGame(game.id))
            )
          )
          .followedBy(_ => game)
      ).followedBy(game =>
        this._games = this._games + (game.id -> game)
        game.id
      )
    }
  override def deleteGame(gameId: Id): Future[Unit] =
    asyncActivityFlatten(s"Delete game '$gameId'")(
      future(games.requireWithId(gameId))
        .compose(game =>
          if game.state.serverSituation != ServerSituation.Terminated
          then game.stop()
          else Future.succeededFuture()
        )
        .followedBy(_ => this._games -= gameId)
    )
  override def findPublicGame(): Future[Id] =
    asyncActivity("Find public game")(
      games.getPublic.getAwaiting.values.headOption match
        case Some(game) => game.id
        case None       => throw NoAvailableGamesException()
    )
  override def findPrivateGame(gameId: Id): Future[Id] =
    asyncActivity(s"Find private game '$gameId'") {
      val privateGames: ChessGameMap = games.getPrivate
      privateGames.getAwaiting.get(gameId) match
        case Some(game) => game.id
        case None =>
          privateGames.getRunning.get(gameId) match
            case Some(game) => throw GameAlreadyStartedException(game.id)
            case None       => throw GameNotFoundException(gameId)
    }
  override def getState(gameId: Id): Future[ServerState] =
    asyncActivityFlatten(s"Get state of game '$gameId'")(
      future(games.requireWithId(gameId)).compose(_.getState)
    )
  override def joinGame(gameId: String, player: LegacyPlayer): Future[Unit] =
    asyncActivityFlatten(s"Join game '$gameId' from player '${player.name}'")(
      future(games.getAwaiting.requireWithId(gameId))
        .compose(game =>
          game
            .join(player)
            .compose(_ =>
              if game.state.serverSituation == ServerSituation.Ready
              then game.start()
              else Future.succeededFuture()
            )
        )
    )
  override def findMoves(gameId: String, position: LegacyPosition): Future[Set[LegacyMove]] =
    asyncActivityFlatten(s"Find moves at position '$position' in game '$gameId'")(
      future(games.getRunning.requireWithId(gameId)).compose(_.findMoves(position))
    )
  override def applyMove(gameId: String, move: LegacyMove): Future[Unit] =
    asyncActivityFlatten(s"Apply move '$move' in game '$gameId'")(
      future(games.getRunning.requireWithId(gameId)).compose(_.applyMove(move))
    )
  override def promote(gameId: String, promotionChoice: PromotionChoice): Future[Unit] =
    asyncActivityFlatten(
      s"Promote pawn to '$promotionChoice' in game '$gameId'"
    )(
      future(games.getRunning.requireWithId(gameId))
        .compose(_.promote(promotionChoice))
    )
  override def subscribe[E <: ChessGameServiceEvent: ClassTag](
      gameId: Id,
      handler: E => Unit
  ): Future[Id] =
    asyncActivityFlatten(s"Subscribe to event ${Event.addressOf[E]} in game '$gameId'")(
      future(games.requireWithId(gameId)).compose(_.subscribe[E](handler))
    )
  override def unsubscribe(gameId: Id, subscriptionIds: Id*): Future[Unit] =
    asyncActivityFlatten(
      s"Cancel subscriptions {#${subscriptionIds.mkString(",#")}} in game '$gameId'"
    )(
      future(games.requireWithId(gameId)).compose(_.unsubscribe(subscriptionIds*))
    )

  /**
   * Send the specified results of the [[ChessGameServer]] with the specified [[Id Id]]
   * to the statistics service.
   *
   * @param gameId the specified [[Id Id]].
   * @param results the specified results.
   * @return a [[Future]] completing when the results of the [[ChessGameServer]] with the
   *         specified [[Id Id]] have been successfully sent to the statistics service.
   */
  private def sendResults(gameId: String, results: ResultMap): Future[Unit] =
    asyncActivityFlatten(s"Send the results of game '$gameId' to the statistics service") {
      future(games.requireWithId(gameId))
        .followedBy(game =>
          LegacyTeam.values.foreach(team =>
            game.configuration
              .playerOption(team)
              .map(_.name)
              .filter(_ != GameConfiguration.GuestPlayerName)
              .foreach(username => statisticsService.addNewScore(username, results.get(team)))
          )
        )
    }

/** Companion object of [[ChessGameModel]]. */
object ChessGameModel:
  extension (self: ChessGameMap) {

    /** @return a [[ChessGameMap]] of the public chess games in this [[ChessGameMap]]. */
    private def getPublic: ChessGameMap = self.filter(_._2.configuration.isPublic)

    /** @return a [[ChessGameMap]] of the private chess games in this [[ChessGameMap]]. */
    private def getPrivate: ChessGameMap = self.filter(_._2.configuration.isPrivate)

    /** @return a [[ChessGameMap]] of the chess games awaiting for players in this [[ChessGameMap]]. */
    private def getAwaiting: ChessGameMap =
      self.filter(_._2.state.serverSituation == ServerSituation.WaitingForPlayers)

    /** @return a [[ChessGameMap]] of the running chess games in this [[ChessGameMap]]. */
    private def getRunning: ChessGameMap =
      self.filter(_._2.state.serverSituation == ServerSituation.Running)

    /**
     * @param gameId the specified game [[Id]].
     * @return the chess game of this [[ChessGameMap]] with the specified game [[Id]].
     * @throws GameNotFoundException if no chess game with the specified [[Id]] is found.
     */
    private def requireWithId(gameId: Id): ChessGameServer =
      self.getOrElse(gameId, throw GameNotFoundException(gameId))
  }
