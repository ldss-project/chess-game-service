package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.game.{GameOverCause as LegacyGameOverCause, Team as LegacyTeam}
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameOver.ResultMap

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

  /** @return the results of this chess game as a [[ResultMap ResultMap]]. */
  def results: ResultMap = winner match
    case Some(winner) =>
      Map(winner.team -> true, winner.team.oppositeTeam -> false)
    case None =>
      Map()

/** Companion object of [[GameOver]]. */
object GameOver:
  /**
   * A map from the team of each player in a chess game to their
   * final result, telling if the player has won or lost.
   *
   * The result can be either:
   *  - `true`: if the player has won;
   *  - `false`: if the player has lost;
   *  - `none`: if the game ended in a par.
   */
  type ResultMap = Map[LegacyTeam, Boolean]

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
