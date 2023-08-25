package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.jahrim.chess.game.service.components.events.ChessGameServiceEvent
import io.github.jahrim.chess.game.service.components.exceptions.*
import io.github.jahrim.chess.game.service.components.ports.model.game.impl.BasicChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState.Id
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice,
  ServerState
}
import io.vertx.core.{Future, Vertx}

import scala.reflect.ClassTag

/** A server hosting a game of chess. */
trait ChessGameServer:
  /**
   * @return the current [[ServerState]] of this [[ChessGameServer]].
   * @note internal use only.
   */
  def state: ServerState

  /**
   * @return the current [[GameConfiguration]] of the chess game in this [[ChessGameServer]].
   * @note internal use only.
   */
  def configuration: GameConfiguration = state.gameState.gameConfiguration

  /**
   * @return the [[Id Id]] of the chess game hosted by this [[ChessGameServer]].
   * @note internal use only.
   */
  def id: Id = configuration.gameIdOption.getOrElse("$Anonymous")

  /**
   * @return true if the [[Id Id]] of the chess game hosted by this [[ChessGameServer]]
   *         has been configured; false otherwise.
   */
  def hasGameId: Boolean = state.gameState.gameConfiguration.gameIdOption.isDefined

  /**
   * @return true if the chess game hosted by this [[ChessGameServer]] has enough players
   *         to start; false otherwise.
   */
  def hasEnoughPlayers: Boolean =
    LegacyTeam.values.forall(team => configuration.playerOption(team).isDefined)

  /**
   * Retrieve the [[ServerState]] of this [[ChessGameServer]]
   *
   * @return a [[Future]] containing the [[ServerState]] of this [[ChessGameServer]].
   *
   *         The [[Future]] completes when this [[ServerState]] of the specified
   *         [[ChessGameServer]] has been successfully retrieved.
   */
  def getState: Future[ServerState]

  /**
   * Apply the specified [[GameConfiguration]] to this [[ChessGameServer]].
   *
   * @param gameConfiguration the specified [[GameConfiguration]].
   * @return a [[Future]] completing when the specified [[GameConfiguration]]
   *         has been successfully applied to this [[ChessGameServer]]. The
   *         [[Future]] fails with a:
   *         - [[GameConfiguredException]]: if this [[ChessGameServer]]
   *           has already been configured.
   */
  def configure(gameConfiguration: GameConfiguration): Future[Unit]

  /**
   * Make the specified [[LegacyPlayer Player]] join this [[ChessGameServer]].
   *
   * @param player the specified [[LegacyPlayer Player]].
   * @return a [[Future]] completing when the specified [[LegacyPlayer Player]]
   *         has successfully joined this [[ChessGameServer]]. The [[Future]]
   *         fails with a:
   *         - [[GameNotWaitingForPlayersException]]: if this [[ChessGameServer]]
   *           is not waiting for players to join (e.g. it's already full or
   *           terminated...).
   *         - [[PlayerAlreadyExistingException]]: if a [[LegacyPlayer Player]]
   *           of the team of the specified [[LegacyPlayer Player]] has already
   *           joined this [[ChessGameServer]].
   */
  def join(player: LegacyPlayer): Future[Unit]

  /**
   * Start this [[ChessGameServer]], enabling the interactions that
   * allow the players to play within the chess game hosted by this
   * [[ChessGameServer]].
   *
   * @return a [[Future]] completing when this [[ChessGameServer]] has
   *         been successfully started. The [[Future]] fails with a:
   *         - [[GameNotReadyException]]: if this [[ChessGameServer]]
   *           is not ready to start.
   */
  def start(): Future[Unit]

  /**
   * Stop this [[ChessGameServer]], removing all the subscriptions to
   * its events and forbidding further interactions.
   *
   * @return a [[Future]] completing when this [[ChessGameServer]] has
   *         been successfully stopped. The [[Future]] fails with a:
   *         - [[GameTerminatedException]]: if this [[ChessGameServer]]
   *           is already terminated.
   */
  def stop(): Future[Unit]

  /**
   * Find the [[LegacyMove Move]]s available for the [[LegacyPiece Piece]]
   * at the specified [[LegacyPosition Position]] within this [[ChessGameServer]].
   *
   * @param position the specified [[LegacyPosition Position]].
   * @return a [[Future]] containing the [[LegacyMove Move]]s of available for
   *         the [[LegacyPiece Piece]] at the specified [[LegacyPosition Position]]
   *         within this [[ChessGameServer]].
   *
   *         The [[Future]] completes when such [[LegacyMove Move]]s have been
   *         evaluated successfully; it fails with a:
   *         - [[GameNotRunningException]]: if this [[ChessGameServer]] is not
   *           running.
   *         - [[GameWaitingForPromotionException]]: if this [[ChessGameServer]]
   *           is running but is waiting for a pawn to be promoted.
   */
  def findMoves(position: LegacyPosition): Future[Set[LegacyMove]]

  /**
   * Apply the specified [[LegacyMove Move]] to this [[ChessGameServer]].
   *
   * @param move the specified [[LegacyMove Move]].
   * @return a [[Future]] completing when the specified [[LegacyMove Move]]
   *         has been successfully applied this [[ChessGameServer]]. The
   *         [[Future]] fails with a:
   *         - [[GameNotRunningException]]: if this [[ChessGameServer]] is not
   *           running.
   *         - [[GameWaitingForPromotionException]]: if this [[ChessGameServer]]
   *           is running but is waiting for a pawn to be promoted.
   */
  def applyMove(move: LegacyMove): Future[Unit]

  /**
   * Promote the pawn currently awaiting for a promotion to the specified
   * [[PromotionChoice]] within this [[ChessGameServer]].
   *
   * @param promotionChoice the specified [[PromotionChoice]].
   * @return a [[Future]] completing when the pawn currently awaiting for a
   *         promotion has been successfully promoted to the specified
   *         [[PromotionChoice]] within this [[ChessGameServer]]. The [[Future]]
   *         fails with a:
   *         - [[GameNotWaitingForPromotionException]]: if this [[ChessGameServer]]
   *           is not waiting for a pawn to be promoted.
   */
  def promote(promotionChoice: PromotionChoice): Future[Unit]

  /**
   * Subscribe to the specified type of [[ChessGameServiceEvent]]s produced by
   * this [[ChessGameServer]].
   *
   * @param handler a consumer for a [[ChessGameServiceEvent]] of the specified
   *                type. This will be run against every event produced of that
   *                type.
   * @tparam E the specified type of [[ChessGameServiceEvent]].
   * @return a [[Future]] containing the [[Id Id]]s of the subscription just
   *         registered, so that it will be possible to cancel the subscription
   *         later on.
   *
   *         The [[Future]] completes when the specified event handler has been
   *         successfully registered to handle the [[ChessGameServiceEvent]]s of
   *         the specified type produced by this [[ChessGameServer]]; it fails
   *         with a:
   *         - [[GameTerminatedException]]: if this [[ChessGameServer]] has ended.
   * @note events are organized hierarchically, so that it is possible to subscribe
   *       to a superclass of events in order to react to all the events that are a
   *       subclass of that superclass. See [[ChessGameServiceEvent]] for more
   *       information about the hierarchy of events of this service.
   */
  def subscribe[E <: ChessGameServiceEvent: ClassTag](handler: E => Unit): Future[Id]

  /**
   * Cancel the subscriptions with the specified subscription [[Id Id]]s
   * registered within this [[ChessGameServer]].
   *
   * @param subscriptionIds the specified subscription [[Id Id]]s.
   * @return a [[Future]] completing when the subscriptions with the specified
   *         subscription [[Id Id]]s registered within this [[ChessGameServer]]
   *         have been successfully cancelled.
   *         The [[Future]] fails with a:
   *         - [[GameTerminatedException]]: if this [[ChessGameServer]] has ended.
   */
  def unsubscribe(subscriptionIds: Id*): Future[Unit]

/** Companion object of chess game. */
object ChessGameServer:
  /**
   * @param vertx the specified [[Vertx]] instance.
   * @return a new [[ChessGameServer]] deployed on the specified [[Vertx]] instance.
   */
  def apply(vertx: Vertx): ChessGameServer = BasicChessGameServer(vertx)
