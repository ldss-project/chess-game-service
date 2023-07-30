package io.github.jahrim.chess.game.service.components.data

import scala.concurrent.duration.Duration

case class TimeConstraintData(timeConstraintType: TimeConstraintTypeData, time: Duration)
