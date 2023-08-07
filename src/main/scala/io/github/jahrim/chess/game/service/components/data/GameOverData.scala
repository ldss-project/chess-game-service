package io.github.jahrim.chess.game.service.components.data

import io.github.chess.engine.model.game.Team

case class GameOverData(cause: CauseData, winner: Option[Team])
