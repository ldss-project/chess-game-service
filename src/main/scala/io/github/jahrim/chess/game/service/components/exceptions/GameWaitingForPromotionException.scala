package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game not to be waiting
 * for a promotion, but the chess game is waiting for a promotion.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameWaitingForPromotionException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameWaitingForPromotionException]]. */
object GameWaitingForPromotionException:
  def apply(gameId: String): GameWaitingForPromotionException =
    new GameWaitingForPromotionException(
      s"Game '$gameId' rejected the request: waiting for promotion."
    )
