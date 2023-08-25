package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.game.Team as LegacyTeam

/**
 * A [[GameStateUpdateEvent]] triggered when the current turn of
 * a chess game is updated.
 *
 * @param turn the new value of the current turn of the chess game.
 */
class TurnUpdateEvent(turn: LegacyTeam) extends GameStateUpdateEvent with Event.Payload(turn)
