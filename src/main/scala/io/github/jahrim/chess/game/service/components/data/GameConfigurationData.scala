package io.github.jahrim.chess.game.service.components.data

import io.github.chess.engine.model.configuration.TimeConstraint

case class GameConfigurationData(
  timeConstraint: Option[TimeConstraint], 
  gameId: Option[String],
  isPrivate: Boolean
)
