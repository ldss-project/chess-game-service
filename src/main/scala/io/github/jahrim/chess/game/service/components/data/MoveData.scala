package io.github.jahrim.chess.game.service.components.data

case class MoveData(
    typeMove: MoveTypeData,
    from: PositionData,
    to: PositionData,
    castling: Option[CastlingData],
    enPassant: Option[EnPassantData]
)
