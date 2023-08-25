package io.github.jahrim.chess.game.service.components.events

import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameOver

/**
 * A [[GameStateUpdateEvent]] triggered when the [[GameOver]] status
 * of a chess game is updated.
 *
 * @param gameOver the new value of the [[GameOver]] status of the
 *                 chess game.
 */
class GameOverUpdateEvent(gameOver: GameOver)
    extends GameStateUpdateEvent
    with Event.Payload(gameOver)
