package io.github.jahrim.chess.game.service.components.events

import io.github.jahrim.chess.game.service.components.ports.model.game.state.MoveHistory

/**
 * A [[GameStateUpdateEvent]] triggered when the [[MoveHistory]]
 * of a chess game is updated.
 *
 * @param history the new value of the [[MoveHistory]] of the
 *                chess game.
 */
class MoveHistoryUpdateEvent(history: MoveHistory)
    extends GameStateUpdateEvent
    with Event.Payload(history)
