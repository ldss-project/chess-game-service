package io.github.jahrim.chess.game.service.components.events

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard

/**
 * A [[GameStateUpdateEvent]] triggered when the [[LegacyChessboard LegacyChessboard]]
 * of a chess game is updated.
 *
 * @param chessboard the new value of the [[LegacyChessboard LegacyChessboard]].
 */
class ChessboardUpdateEvent(chessboard: LegacyChessboard)
    extends GameStateUpdateEvent
    with Event.Payload(chessboard)
