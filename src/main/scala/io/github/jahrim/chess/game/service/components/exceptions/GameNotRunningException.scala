package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game to be running,
 * but the chess game is not running.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameNotRunningException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameNotRunningException]]. */
object GameNotRunningException:
  def apply(gameId: String): GameNotRunningException =
    new GameNotRunningException(s"Game '$gameId' rejected the request: not running.")
