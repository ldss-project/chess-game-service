package io.github.jahrim.chess.game.service.components.ports

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.chess.engine.model.pieces.Piece as LegacyPiece
import io.github.jahrim.chess.game.service.components.events.ChessGameServiceEvent
import io.github.jahrim.chess.game.service.components.exceptions.*
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort.{ChessGameMap, Id}
import io.github.jahrim.chess.game.service.components.ports.model.game.ChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice,
  ServerState
}
import io.github.jahrim.hexarc.architecture.vertx.core.components.Port
import io.vertx.core.Future

import scala.reflect.ClassTag

/**
 * A [[Port]] that handles the configuration, execution and
 * termination of [[ChessGameServer]]s.
 */
trait ChessGamePort extends Port:
  /**
   * Retrieve a [[ChessGameMap ChessGameMap]] of the [[ChessGameServer]]s
   * registered in this service.
   *
   * @return a [[Future]] containing a [[ChessGameMap ChessGameMap]] of
   *         the [[ChessGameServer]]s registered in this service.
   *
   *         The [[Future]] completes when the [[ChessGameServer]]s
   *         registered in this service have been successfully retrieved.
   */
  def getGames: Future[ChessGameMap]

  /**
   * Create a new [[ChessGameServer]] with the specified [[GameConfiguration]].
   *
   * @param gameConfiguration the specified [[GameConfiguration]].
   * @return a [[Future]] containing the [[Id Id]] of the
   *         [[ChessGameServer]] just created.
   *
   *         The [[Future]] succeeds when the [[ChessGameServer]] has
   *         been created; it fails with a:
   *         - [[GameIdAlreadyTakenException]]: if the specified
   *           [[Id Id]] already belongs to one of the registered
   *           [[ChessGameServer]]s.
   */
  def createGame(gameConfiguration: GameConfiguration): Future[Id]

  /**
   * Delete the [[ChessGameServer]] with the specified [[Id Id]].
   * If the [[ChessGameServer]] is not yet terminated, it will be
   * stopped before deletion.
   *
   * @param gameId the specified [[Id Id]].
   * @return a [[Future]] completing when the [[ChessGameServer]] has
   *         been successfully deleted. The [[Future]] fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]] does
   *           not belong to any of the registered [[ChessGameServer]]s.
   */
  def deleteGame(gameId: Id): Future[Unit]

  /**
   * Search for a random public [[ChessGameServer]] that is still awaiting
   * for players to join.
   *
   * @return a [[Future]] containing the [[Id Id]] of a random
   *         [[ChessGameServer]] that is still waiting for players to join.
   *
   *         The [[Future]] completes when such [[ChessGameServer]] is found;
   *         it fails with a:
   *         - [[NoAvailableGamesException]]: if no such [[ChessGameServer]]
   *           is found.
   */
  def findPublicGame(): Future[Id]

  /**
   * Search for a private [[ChessGameServer]] with the specified [[Id Id]]
   * that is still waiting for players to join.
   *
   * @param gameId the specified [[Id Id]].
   * @return a [[Future]] containing the [[Id Id]] of the requested
   *         private [[ChessGameServer]] that is still waiting for players
   *         to join.
   *
   *         The [[Future]] completes when such [[ChessGameServer]] is found;
   *         it fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]]
   *           does not belong to any of the registered [[ChessGameServer]]s;
   *         - [[GameAlreadyStartedException]]: if a [[ChessGameServer]] with
   *           the specified [[Id Id]] is found but it has already started.
   */
  def findPrivateGame(gameId: Id): Future[Id]

  /**
   * Retrieve the [[ServerState]] of the [[ChessGameServer]] with the specified
   * [[Id Id]].
   *
   * @param gameId the specified [[Id Id]].
   * @return a [[Future]] containing the [[ServerState]] of the [[ChessGameServer]]
   *         with the specified [[Id Id]].
   *
   *         The [[Future]] completes when the [[ServerState]] of the specified
   *         [[ChessGameServer]] has been successfully retrieved; it fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]]
   *           does not belong to any of the registered [[ChessGameServer]]s.
   */
  def getState(gameId: Id): Future[ServerState]

  /**
   * Make the specified [[LegacyPlayer Player]] join the [[ChessGameServer]] with
   * the specified [[Id Id]].
   *
   * @param gameId the specified [[Id Id]].
   * @param player the specified [[LegacyPlayer Player]].
   * @return a [[Future]] completing when the specified [[LegacyPlayer Player]]
   *         has successfully joined the [[ChessGameServer]] with the specified
   *         [[Id Id]]. The [[Future]] fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]]
   *           does not belong to any of the registered [[ChessGameServer]]s.
   *         - [[GameNotWaitingForPlayersException]]: if the [[ChessGameServer]]
   *           with the specified [[Id Id]] is not waiting for players to join
   *           (e.g. it's already full or terminated...).
   *         - [[PlayerAlreadyExistingException]]: if a [[LegacyPlayer Player]]
   *           of the team of the specified [[LegacyPlayer Player]] has already
   *           joined the [[ChessGameServer]] with the specified [[Id Id]].
   */
  def joinGame(gameId: Id, player: LegacyPlayer): Future[Unit]

  /**
   * Find the [[LegacyMove Move]]s available for the [[LegacyPiece Piece]]
   * at the specified [[LegacyPosition Position]] within the [[ChessGameServer]]
   * with the specified [[Id Id]].
   *
   * @param gameId   the specified [[Id Id]].
   * @param position the specified [[LegacyPosition Position]].
   * @return a [[Future]] containing the [[LegacyMove Move]]s of available for
   *         the [[LegacyPiece Piece]] at the specified [[LegacyPosition Position]]
   *         within the [[ChessGameServer]] with the specified [[Id Id]].
   *
   *         The [[Future]] completes when such [[LegacyMove Move]]s have been
   *         evaluated successfully; it fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]]
   *           does not belong to any of the registered [[ChessGameServer]]s.
   *         - [[GameNotRunningException]]: if the [[ChessGameServer]] with the
   *           specified [[Id Id]] is not running.
   *         - [[GameWaitingForPromotionException]]: if the [[ChessGameServer]]
   *           with the specified [[Id Id]] is running but is waiting for a pawn
   *           to be promoted.
   */
  def findMoves(gameId: Id, position: LegacyPosition): Future[Set[LegacyMove]]

  /**
   * Apply the specified [[LegacyMove Move]] to the [[ChessGameServer]] with the
   * specified [[Id Id]].
   *
   * @param gameId the specified [[Id Id]].
   * @param move   the specified [[LegacyMove Move]].
   * @return a [[Future]] completing when the specified [[LegacyMove Move]]
   *         has been successfully applied the [[ChessGameServer]] with the
   *         specified [[Id Id]]. The [[Future]] fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]] does not
   *           belong to any of the registered [[ChessGameServer]]s.
   *         - [[GameNotRunningException]]: if the [[ChessGameServer]] with the
   *           specified [[Id Id]] is not running.
   *         - [[GameWaitingForPromotionException]]: if the [[ChessGameServer]]
   *           with the specified [[Id Id]] is running but is waiting for a pawn
   *           to be promoted.
   */
  def applyMove(gameId: Id, move: LegacyMove): Future[Unit]

  /**
   * Promote the pawn currently awaiting for a promotion to the specified
   * [[PromotionChoice]] within the [[ChessGameServer]] with the specified [[Id Id]].
   *
   * @param gameId          the specified [[Id Id]].
   * @param promotionChoice the specified [[PromotionChoice]].
   * @return a [[Future]] completing when the pawn currently awaiting for a
   *         promotion has been successfully promoted to the specified
   *         [[PromotionChoice]] within the [[ChessGameServer]] with the specified
   *         [[Id Id]]. The [[Future]] fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]] does not
   *           belong to any of the registered [[ChessGameServer]]s.
   *         - [[GameNotWaitingForPromotionException]]: if the [[ChessGameServer]]
   *           with the specified [[Id Id]] is not waiting for a pawn to be promoted.
   */
  def promote(gameId: Id, promotionChoice: PromotionChoice): Future[Unit]

  /**
   * Subscribe to the specified type of [[ChessGameServiceEvent]]s produced by
   * the [[ChessGameServer]] with the specified [[Id Id]].
   *
   * @param gameId  the specified [[Id Id]].
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
   *         the specified type produced by the [[ChessGameServer]] with the
   *         specified [[Id Id]]; it fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]]
   *           does not belong to any of the registered [[ChessGameServer]]s.
   *         - [[GameTerminatedException]]: if the [[ChessGameServer]] with the
   *           specified [[Id Id]] has ended.
   * @note events are organized hierarchically, so that it is possible to subscribe
   *       to a superclass of events in order to react to all the events that are a
   *       subclass of that superclass. See [[ChessGameServiceEvent]] for more
   *       information about the hierarchy of events of this service.
   */
  def subscribe[E <: ChessGameServiceEvent: ClassTag](gameId: Id, handler: E => Unit): Future[Id]

  /**
   * Cancel the subscriptions with the specified subscription [[Id Id]]s
   * registered within the [[ChessGameServer]] with the specified game [[Id Id]].
   *
   * @param gameId          the specified game [[Id Id]].
   * @param subscriptionIds the specified subscription [[Id Id]]s.
   * @return a [[Future]] completing when the subscriptions with the specified
   *         subscription [[Id Id]]s registered within the [[ChessGameServer]] with
   *         the specified game [[Id Id]] have been successfully cancelled.
   *         The [[Future]] fails with a:
   *         - [[GameNotFoundException]]: if the specified [[Id Id]] does not
   *           belong to any of the registered [[ChessGameServer]]s.
   *         - [[GameTerminatedException]]: if the [[ChessGameServer]] with the
   *           specified [[Id Id]] has ended.
   */
  def unsubscribe(gameId: Id, subscriptionIds: Id*): Future[Unit]

/** Companion object of [[ChessGamePort]]. */
object ChessGamePort:
  /** An identifier for an entity. */
  type Id = String

  /** A map from game [[Id Id]]s to [[ChessGameServer]]s. */
  type ChessGameMap = Map[Id, ChessGameServer]
