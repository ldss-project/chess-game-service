package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.configuration.WhitePlayer as LegacyWhitePlayer

import scala.concurrent.duration.Duration

/**
 * A [[TimerUpdateEvent]] triggered when the timer of the
 * [[LegacyWhitePlayer LegacyWhitePlayer]] in a chess game
 * is updated.
 *
 * @param duration the new value of the timer of the
 *                 [[LegacyWhitePlayer LegacyWhitePlayer]]
 *                 in the chess game.
 */
class WhiteTimerUpdateEvent(duration: Duration) extends TimerUpdateEvent(duration)
