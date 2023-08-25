package io.github.jahrim.chess.game.service.components.events

/**
 * A [[ChessGameServiceEvent]] triggered when the subscriptions
 * of a server hosting a chess game are updated.
 *
 * @param subscriptions the new value of the subscriptions of the
 *                      server.
 */
class SubscriptionUpdateEvent(subscriptions: Iterable[String])
    extends ServerStateUpdateEvent
    with Event.Payload(subscriptions.toSeq)
