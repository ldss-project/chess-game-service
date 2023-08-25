package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.configuration.BlackPlayer as LegacyBlackPlayer

/**
 * A [[PlayerUpdateEvent]] triggered when the [[LegacyBlackPlayer LegacyBlackPlayer]]
 * of a chess game is updated.
 *
 * @param player the new value of the [[LegacyBlackPlayer LegacyBlackPlayer]] playing
 *               in the chess game.
 */
class BlackPlayerUpdateEvent(player: LegacyBlackPlayer) extends PlayerUpdateEvent(player)
