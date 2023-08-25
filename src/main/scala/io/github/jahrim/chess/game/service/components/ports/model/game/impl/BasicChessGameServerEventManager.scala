package io.github.jahrim.chess.game.service.components.ports.model.game.impl

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  Player as LegacyPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.chess.util.scala.id.Id as LegacyId
import io.github.jahrim.chess.game.service.components.data.codecs.vertx.Codecs as VertxCodecs
import io.github.jahrim.chess.game.service.components.events.*
import io.github.jahrim.chess.game.service.components.exceptions.{
  ChessGameServiceException,
  InternalServerException
}
import io.github.jahrim.chess.game.service.components.ports.model.game.ChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.impl.BasicChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState.{
  Id,
  SubscriptionMap
}
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameOver,
  GameSituation,
  MoveHistory,
  ServerSituation
}
import io.github.jahrim.chess.game.service.util.receiver.Receiver
import io.github.jahrim.hexarc.logging.LoggerFactory
import io.vertx.core.Future
import org.slf4j.Logger

import scala.concurrent.duration.Duration
import scala.reflect.{ClassTag, classTag}
import scala.util.Failure

/**
 * A mixin that handles the subscriptions of the clients of a
 * [[BasicChessGameServer]] and the propagation of [[ChessGameServiceEvent]]s
 *
 * This mixin binds the updates that are applied to the state of the
 * [[BasicChessGameServer]] to the propagation of the corresponding
 * [[ChessGameServiceEvent]]s, using a set of [[Receiver]]s.
 *
 * In this way, each update of the state of the [[BasicChessGameServer]]
 * results in the propagation of a corresponding event that will notify
 * the subscribers of that update, keeping the client-side state of the
 * game always up-to-date with the server-side state of the game. That is
 * only if the state is updated using the provided [[Receiver]]s.
 */
trait BasicChessGameServerEventManager extends ChessGameServer:
  self: BasicChessGameServer =>

  activity("Register Vertx codecs", BasicChessGameServerEventManager.Logger.info(_))(
    VertxCodecs.registerInto(self.vertx)
  )

  override def subscribe[E <: ChessGameServiceEvent: ClassTag](handler: E => Unit): Future[Id] =
    asyncActivity(s"Subscribe to ${Event.addressOf[E]}") {
      onlyIfNotTerminated {
        val subscriptionId: Id = LegacyId()
        subscriptions << (
          self._state.subscriptions + (
            subscriptionId ->
              vertx.eventBus.consumer[E](
                s"${self.id}:${Event.addressOf[E]}",
                message => handler(message.body)
              )
          )
        )
        subscriptionId
      }
    }

  override def unsubscribe(subscriptionIds: Id*): Future[Unit] =
    asyncActivityFlatten(s"Try cancelling subscriptions {#${subscriptionIds.mkString(",#")}}")(
      onlyIfNotTerminated { forcedUnsubscribe(subscriptionIds*) }
    )

  /** As [[unsubscribe]], but it can be invoked in any state of the [[ChessGameServer]]. */
  protected def forcedUnsubscribe(subscriptionIds: Id*): Future[Unit] =
    asyncActivity(s"Cancel subscriptions {#${subscriptionIds.mkString(",#")}}")(
      subscriptions << (
        self._state.subscriptions --
          self._state.subscriptions
            .filter((key, _) => subscriptionIds.contains(key))
            .tapEach(_._2.unregister())
            .keys
      )
    )

  /**
   * A [[Receiver]] that binds the logging of a message
   * to the publication of a [[LoggingEvent]].
   */
  protected def logMessage: Receiver[String] =
    Receiver.atomic(value =>
      self.logger.info(value)
      publishSilently(LoggingEvent(value))
    )

  /**
   * A [[Receiver]] that binds the update of the [[ServerSituation]]
   * of this [[BasicChessGameServer]] to the publication of a
   * [[ServerSituationUpdateEvent]].
   */
  protected def serverSituation: Receiver[ServerSituation] =
    Receiver.atomic(value =>
      self._state = self._state.setServerSituation(value)
      publish(ServerSituationUpdateEvent(value))
    )

  /**
   * A [[Receiver]] that binds the update of the latest error thrown
   * by this [[BasicChessGameServer]] to the publication of a
   * [[ServerErrorUpdateEvent]].
   */
  protected def serverError: Receiver[Throwable] =
    Receiver.atomic(value =>
      val chessGameServiceException: ChessGameServiceException = value match
        case e: ChessGameServiceException => e
        case e                            => InternalServerException(e)
      self._state = self._state.setServerError(Some(chessGameServiceException))
      chessGameServiceException.printStackTrace()
      publish(ServerErrorUpdateEvent(chessGameServiceException))
    )

  /**
   * A [[Receiver]] that binds the update of the current turn
   * of this [[BasicChessGameServer]] to the publication of a
   * [[TurnUpdateEvent]].
   */
  protected def currentTurn: Receiver[LegacyTeam] =
    Receiver.atomic(value =>
      self._state = self._state.setCurrentTurn(value)
      publish(TurnUpdateEvent(value))
    )

  /**
   * A [[Receiver]] that binds the update of the [[GameSituation]]
   * of this [[BasicChessGameServer]] to the publication of a
   * [[GameSituationUpdateEvent]].
   */
  protected def gameSituation: Receiver[GameSituation] =
    Receiver.atomic(value =>
      self._state = self._state.setGameSituation(value)
      publish(GameSituationUpdateEvent(value))
    )

  /**
   * A [[Receiver]] that binds the update of the [[GameOver]]
   * status of this [[BasicChessGameServer]] to the publication
   * of a [[GameOverUpdateEvent]].
   */
  protected def gameOver: Receiver[GameOver] =
    Receiver.atomic(value =>
      self._state = self._state.setGameOver(value)
      publish(GameOverUpdateEvent(value))
      self.stop()
    )

  /**
   * A [[Receiver]] that binds the update of a
   * [[LegacyPlayer LegacyPlayer]] of this [[BasicChessGameServer]]
   * to the publication of a [[PlayerUpdateEvent]].
   */
  protected def player: Receiver[LegacyPlayer] =
    Receiver.atomic(value =>
      self._state = self._state.setGameConfiguration {
        value match
          case whitePlayer: LegacyWhitePlayer =>
            publish(WhitePlayerUpdateEvent(whitePlayer))
            self._state.gameState.gameConfiguration.setWhitePlayer(whitePlayer)
          case blackPlayer: LegacyBlackPlayer =>
            publish(BlackPlayerUpdateEvent(blackPlayer))
            self._state.gameState.gameConfiguration.setBlackPlayer(blackPlayer)
      }
    )

  /**
   * A [[Receiver]] that binds the update of the
   * [[LegacyChessboard LegacyChessboard]] of this [[BasicChessGameServer]]
   * to the publication of a [[ChessboardUpdateEvent]].
   */
  protected def chessboard: Receiver[LegacyChessboard] =
    Receiver.atomic(value =>
      self._state = self._state.setChessboard(value)
      publish(ChessboardUpdateEvent(value))
    )

  /**
   * A [[Receiver]] that binds the update of the [[MoveHistory]]
   * of this [[BasicChessGameServer]] to the publication of a
   * [[MoveHistoryUpdateEvent]].
   */
  protected def moveHistory: Receiver[MoveHistory] =
    Receiver.atomic(value =>
      self._state = self._state.setMoveHistory(value)
      publish(MoveHistoryUpdateEvent(value))
    )

  /**
   * A [[Receiver]] that binds the update of the timer of a player
   * of this [[BasicChessGameServer]] to the publication of a
   * [[TimerUpdateEvent]].
   */
  protected def timer: Receiver[(LegacyTeam, Duration)] =
    Receiver.atomic((team, time) =>
      self._state = self._state.setTimers(self._state.gameState.timers + (team -> time))
      team match
        case LegacyTeam.WHITE => publish(WhiteTimerUpdateEvent(time))
        case LegacyTeam.BLACK => publish(BlackTimerUpdateEvent(time))
    )

  /**
   * A [[Receiver]] that binds the update of the subscriptions
   * of this [[BasicChessGameServer]] to the publication of a
   * [[SubscriptionUpdateEvent]].
   */
  protected def subscriptions: Receiver[SubscriptionMap] =
    Receiver.atomic(value =>
      self._state = self._state.setSubscriptions(value)
      publish(SubscriptionUpdateEvent(value.keys))
    )

  /**
   * Publish the specified [[ChessGameServiceEvent]] notifying all
   * the corresponding subscribers.
   *
   * @param event the specified [[ChessGameServiceEvent]].
   * @tparam E the type of the specified [[ChessGameServiceEvent]].
   */
  private def publish[E <: ChessGameServiceEvent: ClassTag](event: E): Unit =
    activity(s"Publish event: $event") { publishSilently(event) }

  /** As [[publish]] but it avoids logging the activity. */
  private def publishSilently[E <: ChessGameServiceEvent: ClassTag](event: E): Unit =
    Event.addressesOf[E].foreach { address =>
      self.vertx.eventBus.publish(s"${self.id}:$address", event)
    }

/** Companion object of [[BasicChessGameServerEventManager]]. */
object BasicChessGameServerEventManager:
  /** Default logger for a [[BasicChessGameServerEventManager]]. */
  private val Logger: Logger =
    LoggerFactory.fromClass(classOf[BasicChessGameServerEventManager], false)
