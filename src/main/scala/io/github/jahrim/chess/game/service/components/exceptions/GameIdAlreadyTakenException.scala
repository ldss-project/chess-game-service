package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * create a chess game with an id that is already taken.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameIdAlreadyTakenException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameIdAlreadyTakenException]]. */
object GameIdAlreadyTakenException:
  def apply(gameId: String): GameIdAlreadyTakenException =
    new GameIdAlreadyTakenException(s"Id '$gameId' already taken.")
