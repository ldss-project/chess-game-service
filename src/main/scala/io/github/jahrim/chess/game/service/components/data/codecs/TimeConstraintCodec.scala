package io.github.jahrim.chess.game.service.components.data.codecs

import DurationCodec.given
import TimeConstraintTypeDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.TimeConstraintData
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import io.github.jahrim.chess.game.service.components.data.TimeConstraintTypeData

/** [[Bson]] codec for [[TimeConstraintData]]. */
object TimeConstraintCodec:
  /** A given [[BsonDocumentDecoder]] for [[TimeConstraintData]]. */
  given bsonToTimeConstraint: BsonDocumentDecoder[TimeConstraintData] = bson =>
    TimeConstraintData(
      timeConstraintType =
        bson.require("gameConfiguration.timeConstraint").as[TimeConstraintTypeData],
      time = bson.require("gameConfiguration.timeConstraint").as[Duration]
    )

  /** A given [[BsonDocumentEncoder]] for [[TimeConstraintData]]. */
  given timeConstraintToBson: BsonDocumentEncoder[TimeConstraintData] = tc =>
    bson {
      "type" :: tc.timeConstraintType
      "time" :: tc.time
    }
