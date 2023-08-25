package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game not to be terminated,
 * but the chess game is terminated.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameTerminatedException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameTerminatedException]]. */
object GameTerminatedException:
  def apply(gameId: String): GameTerminatedException =
    new GameTerminatedException(s"Game '$gameId' rejected the request: terminated.")
