package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a chess game is queried
 * but no chess games are available in this service.
 *
 * @param message a detailed description of the [[Exception]].
 */
class NoAvailableGamesException(message: String) extends ChessGameServiceException(message)

/** Companion object [[NoAvailableGamesException]]. */
object NoAvailableGamesException:
  def apply(): NoAvailableGamesException = new NoAvailableGamesException(
    "No available games to join."
  )
