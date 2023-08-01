package io.github.jahrim.chess.game.service.components.data

case class SituationData(
    situationType: SituationTypeData,
    promotingPawnPosition: Option[PositionData]
)
