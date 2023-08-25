package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * find a private chess game when it has already started.
 */
class GameAlreadyStartedException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameAlreadyStartedException]]. */
object GameAlreadyStartedException:
  def apply(gameId: String): GameAlreadyStartedException =
    new GameAlreadyStartedException(s"Game with id '$gameId' has already started.")
