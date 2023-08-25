package io.github.jahrim.chess.game.service.components.events

import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameSituation

/**
 * A [[GameStateUpdateEvent]] triggered when the [[GameSituation]]
 * of a chess game is updated.
 *
 * @param situation the new value of the [[GameSituation]] of the
 *                  chess game.
 */
class GameSituationUpdateEvent(situation: GameSituation)
    extends GameStateUpdateEvent
    with Event.Payload(situation)
