package io.github.jahrim.chess.game.service.components.events

import scala.concurrent.duration.Duration

/**
 * A [[GameStateUpdateEvent]] triggered when the timer of a
 * player in a chess game is updated.
 *
 * @param duration the new value of the timer of a player
 *                 in the chess game.
 */
class TimerUpdateEvent(duration: Duration) extends GameStateUpdateEvent with Event.Payload(duration)
