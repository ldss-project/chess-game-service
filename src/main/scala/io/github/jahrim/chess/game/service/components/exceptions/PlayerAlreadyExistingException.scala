package io.github.jahrim.chess.game.service.components.exceptions

import io.github.chess.engine.model.game.Team as LegacyTeam

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * join a chess game as a player of a specified team when a player
 * of that team already exists.
 *
 * @param message a detailed description of the [[Exception]].
 */
class PlayerAlreadyExistingException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[PlayerAlreadyExistingException]]. */
object PlayerAlreadyExistingException:
  def apply(gameId: String, team: LegacyTeam): PlayerAlreadyExistingException =
    new PlayerAlreadyExistingException(
      s"Player '$team' already existing in game with id '$gameId'."
    )
