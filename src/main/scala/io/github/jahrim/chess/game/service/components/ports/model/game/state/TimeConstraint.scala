/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele, Kentpayeva Madina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
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
