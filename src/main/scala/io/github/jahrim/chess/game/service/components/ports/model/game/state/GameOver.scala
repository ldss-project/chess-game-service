package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.game.GameOverCause as LegacyGameOverCause

/** The game over status of a chess game. */
trait GameOver:
  /** @return the [[LegacyGameOverCause LegacyGameOverCause]] of this [[GameOver]]. */
  def cause: LegacyGameOverCause

  /**
   * @return a [[Some]] containing the [[LegacyPlayer LegacyPlayer]] who won the
   *         chess game if present; a [[None]] if the chess game ended in
   *         a par.
   */
  def winner: Option[LegacyPlayer]

/** Companion object of [[GameOver]]. */
object GameOver:
  /**
   * Create a new [[GameOver]] status for a chess game.
   *
   * @param cause  the [[LegacyGameOverCause LegacyGameOverCause]] of the [[GameOver]].
   * @param winner a [[Some]] containing the [[LegacyPlayer LegacyPlayer]] who won the
   *               chess game if present; a [[None]] if the chess game ended in
   *               a par.
   * @return a new [[GameOver]] status for a chess game.
   */
  def apply(cause: LegacyGameOverCause, winner: Option[LegacyPlayer] = None): GameOver =
    BasicGameOver(cause, winner)

  /** Basic implementation of [[GameOver]]. */
  private case class BasicGameOver(
      override val cause: LegacyGameOverCause,
      override val winner: Option[LegacyPlayer]
  ) extends GameOver
