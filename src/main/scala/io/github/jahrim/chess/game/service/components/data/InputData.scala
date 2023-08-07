package io.github.jahrim.chess.game.service.components.data

case class InputData(
    findMoves: Option[FindMovesData],
    applyMove: Option[ApplyMoveData],
    promote: Option[PromoteData]
)
