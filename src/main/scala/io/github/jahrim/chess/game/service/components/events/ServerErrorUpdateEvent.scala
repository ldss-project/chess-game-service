package io.github.jahrim.chess.game.service.components.events

import io.github.jahrim.chess.game.service.components.exceptions.ChessGameServiceException

/**
 * A [[ServerStateUpdateEvent]] triggered when the latest error
 * thrown by a server hosting a chess game is updated.
 *
 * @param latestError the new value of the latest error thrown
 *                    by the server.
 */
class ServerErrorUpdateEvent(latestError: ChessGameServiceException)
    extends ServerStateUpdateEvent
    with Event.Payload(latestError)
