package io.github.jahrim.chess.game.service.components.ports

import io.github.chess.engine.events.Event
import io.github.chess.engine.model.board.Position
import io.github.chess.engine.model.configuration.Player
import io.github.chess.engine.model.moves.Move
import io.github.chess.engine.model.pieces.{Pawn, Piece, PromotionPiece}
import io.github.jahrim.chess.game.service.components.data.GameConfigurationData
import io.github.jahrim.hexarc.architecture.vertx.core.components.Port
import io.vertx.core.Future
import scala.reflect.ClassTag

trait ChessGamePort extends Port:
  // Game Manager
  /**
   * Starts a game with the specified [[GameConfigurationData]].
   *
   * @param gameConfiguration the specified [[GameConfigurationData]]
   * @return a future containing the id of the game.
   */
  def createGame(gameConfiguration: GameConfigurationData): Future[String]

  /**
   * @return a future containing the id of a public game that is
   *         waiting for players
   */
  def findPublicGame(): Future[String]

  /**
   * @param gameId the specified id of the game
   * @return a future containing the specified id if a private game is
   *         bound to it
   */
  def findPrivateGame(gameId: String): Future[String]

  // Game
  /**
   * @param gameId the id of the specified game
   * @param player the specified player
   * @return a future that completes when the specified player
   *         has joined the specified game
   */
  def joinGame(gameId: String, player: Player): Future[Unit]

  /**
   * @param gameId the id of the specified game
   * @param position the specified position
   * @return a future containing all the possible moves that are available from the
   *         specified position in the specified game
   */
  def findMoves(gameId: String, position: Position): Future[Set[Move]]

  /**
   * @param gameId the id of the specified game
   * @param move the specified move
   * @return a future that completes when the specified move has been applied
   *         in the specified game
   */
  def applyMove(gameId: String, move: Move): Future[Unit]

  /**
   * @param gameId the id of the specified game
   * @param pawnPosition the position of the pawn to promote
   * @param promotingPiece the piece to promote the pawn to
   * @return a future containing the piece that the pawn was promoted to
   */
  def promote[P <: Piece](
      gameId: String,
      pawnPosition: Position,
      promotingPiece: PromotionPiece[P]
  ): Future[P]

  /**
   * Subscribes an handler to a particular event.
   *
   * @param handler a consumer for the specified event
   * @tparam T type parameter of the event extending the superclass [[Event]]
   * @return a future containing the id of the subscription
   */
  def subscribe[T <: Event: ClassTag](handler: T => Unit): Future[String]

  /**
   * Cancel the specified subscriptions.
   *
   * @param subscriptionIds the ids of the specified subscriptions
   * @return a future that completes when the cancellation has completed
   */
  def unsubscribe(subscriptionIds: String*): Future[Unit]
