package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.configuration.WhitePlayer as LegacyWhitePlayer

/**
 * A [[PlayerUpdateEvent]] triggered when the [[LegacyWhitePlayer LegacyWhitePlayer]]
 * of a chess game is updated.
 *
 * @param player the new value of the [[LegacyWhitePlayer LegacyWhitePlayer]] playing
 *               in the chess game.
 */
class WhitePlayerUpdateEvent(player: LegacyWhitePlayer) extends PlayerUpdateEvent(player)
