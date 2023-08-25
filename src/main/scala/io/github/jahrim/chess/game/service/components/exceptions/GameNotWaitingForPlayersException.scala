package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game to be waiting for
 * players, but the chess game is not waiting for players.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameNotWaitingForPlayersException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameNotWaitingForPlayersException]]. */
object GameNotWaitingForPlayersException:
  def apply(gameId: String): GameNotWaitingForPlayersException =
    new GameNotWaitingForPlayersException(
      s"Game '$gameId' rejected the request: not waiting for players."
    )
