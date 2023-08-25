package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game to be ready,
 * but the chess game is not ready.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameNotReadyException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameNotReadyException]]. */
object GameNotReadyException:
  def apply(gameId: String): GameNotReadyException =
    new GameNotReadyException(s"Game '$gameId' rejected the request: not ready.")
