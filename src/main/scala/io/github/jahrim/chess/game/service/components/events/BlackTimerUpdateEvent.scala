package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.configuration.BlackPlayer as LegacyBlackPlayer

import scala.concurrent.duration.Duration

/**
 * A [[TimerUpdateEvent]] triggered when the timer of the
 * [[LegacyBlackPlayer LegacyBlackPlayer]] in a chess game
 * is updated.
 *
 * @param duration the new value of the timer of the
 *                 [[LegacyBlackPlayer LegacyBlackPlayer]]
 *                 in the chess game.
 */
class BlackTimerUpdateEvent(duration: Duration) extends TimerUpdateEvent(duration)
