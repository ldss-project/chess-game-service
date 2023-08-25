package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.configuration.{
  Player as LegacyPlayer,
  TimeConstraint as LegacyTimeConstraint
}
import io.github.chess.engine.model.moves.Move as LegacyMove

/** Adapter of a [[LegacyTimeConstraint LegacyTimeConstraint]]. */
enum TimeConstraint:
  /** A [[TimeConstraint]] where no constraints on the time are enforced. */
  case NoLimit

  /**
   * A [[TimeConstraint]] where the timer of a [[LegacyPlayer Player]]
   * refreshes at every [[LegacyMove Move]].
   */
  case MoveLimit

  /**
   * A [[TimeConstraint]] where the timer of a [[LegacyPlayer Player]]
   * never refreshes.
   */
  case PlayerLimit

  /** The [[LegacyTimeConstraint LegacyTimeConstraint]] adapted by this [[TimeConstraint]]. */
  def legacy: LegacyTimeConstraint = this match
    case TimeConstraint.NoLimit     => LegacyTimeConstraint.NoLimit
    case TimeConstraint.MoveLimit   => LegacyTimeConstraint.MoveLimit
    case TimeConstraint.PlayerLimit => LegacyTimeConstraint.PlayerLimit

  /**
   * Set the duration of this [[TimeConstraint]] to the specified duration.
   * @param minutes the specified duration in minutes.
   * @return this.
   */
  def withDuration(minutes: Int): this.type =
    legacy.minutes = minutes
    this

/** Companion object of [[TimeConstraint]]. */
object TimeConstraint:
  /**
   * @param legacy the specified [[LegacyTimeConstraint LegacyTimeConstraint]].
   * @return a new [[TimeConstraint]] built from the specified
   *         [[LegacyTimeConstraint LegacyTimeConstraint]].
   */
  def fromLegacy(legacy: LegacyTimeConstraint): TimeConstraint =
    (legacy match
      case LegacyTimeConstraint.NoLimit     => TimeConstraint.NoLimit
      case LegacyTimeConstraint.MoveLimit   => TimeConstraint.MoveLimit
      case LegacyTimeConstraint.PlayerLimit => TimeConstraint.PlayerLimit
    ).withDuration(legacy.minutes)
