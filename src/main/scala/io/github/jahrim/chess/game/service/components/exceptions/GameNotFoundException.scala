package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a chess game with a specific
 * id is queried but not found.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameNotFoundException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameNotFoundException]]. */
object GameNotFoundException:
  def apply(gameId: String): GameNotFoundException =
    new GameNotFoundException(s"Game '$gameId' not found.")
