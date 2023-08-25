package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game not to be configured,
 * but the chess game is configured.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameConfiguredException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[ChessGameServiceException]]. */
object GameConfiguredException:
  def apply(gameId: String): GameConfiguredException =
    new GameConfiguredException(s"Game '$gameId' rejected the request: configured.")
