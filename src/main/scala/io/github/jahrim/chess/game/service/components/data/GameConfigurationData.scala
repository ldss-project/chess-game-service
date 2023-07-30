package io.github.jahrim.chess.game.service.components.data

case class GameConfigurationData(
    timeConstraint: Option[TimeConstraintData],
    gameId: Option[String],
    isPrivate: Boolean
)
