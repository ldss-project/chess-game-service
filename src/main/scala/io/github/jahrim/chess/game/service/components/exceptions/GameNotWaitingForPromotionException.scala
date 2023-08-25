package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game to be waiting for
 * a promotion, but the chess game is not waiting for a promotion.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameNotWaitingForPromotionException(message: String)
    extends ChessGameServiceException(message)

/** Companion object of [[GameNotWaitingForPlayersException]]. */
object GameNotWaitingForPromotionException:
  def apply(gameId: String): GameNotWaitingForPromotionException =
    new GameNotWaitingForPromotionException(
      s"Game '$gameId' rejected the request: not waiting for promotion."
    )
