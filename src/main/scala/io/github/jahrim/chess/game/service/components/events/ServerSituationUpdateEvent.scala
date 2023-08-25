package io.github.jahrim.chess.game.service.components.events

import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation

/**
 * A [[ServerStateUpdateEvent]] triggered when the [[ServerSituation]] of a
 * server hosting a chess game is updated.
 *
 * @param serverSituation the new value of the [[ServerSituation]] of
 *                        the server.
 */
class ServerSituationUpdateEvent(serverSituation: ServerSituation)
    extends ServerStateUpdateEvent
    with Event.Payload(serverSituation)
