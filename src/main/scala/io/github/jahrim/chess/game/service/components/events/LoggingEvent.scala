package io.github.jahrim.chess.game.service.components.events

/**
 * A [[ChessGameServiceEvent]] triggered when the chess game service
 * logs a new message.
 *
 * @param message the new message logged by the chess game service.
 */
class LoggingEvent(message: String) extends ChessGameServiceEvent with Event.Payload(message)
