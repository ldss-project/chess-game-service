package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.configuration.Player as LegacyPlayer

/**
 * A [[GameStateUpdateEvent]] triggered when a [[LegacyPlayer LegacyPlayer]]
 * of a chess game is updated.
 *
 * @param player the new value for one of the [[LegacyPlayer LegacyPlayer]]s
 *               in the chess game.
 */
class PlayerUpdateEvent(player: LegacyPlayer)
    extends GameStateUpdateEvent
    with Event.Payload(player)
