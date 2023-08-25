package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.GameOverCause as LegacyGameOverCause
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyGameOverCause LegacyGameOverCause]]. */
object LegacyGameOverCauseCodec:
  /** A given [[BsonDecoder]] for [[LegacyGameOverCause LegacyGameOverCause]]. */
  given gameOverCauseDecoder: BsonDecoder[LegacyGameOverCause] = bson =>
    LegacyGameOverCause.valueOf(bson.asString.getValue)

  /** A given [[BsonEncoder]] for [[LegacyGameOverCause LegacyGameOverCause]]. */
  given gameOverCauseEncoder: BsonEncoder[LegacyGameOverCause] = gameOverCause =>
    gameOverCause.toString.asBson
