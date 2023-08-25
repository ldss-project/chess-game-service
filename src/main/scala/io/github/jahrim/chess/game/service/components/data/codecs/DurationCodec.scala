package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.TimeConstraint
import io.github.chess.util.general.timer.Time
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[Duration]]. */
object DurationCodec:
  /** A given [[BsonDecoder]] for [[Duration]]. */
  given durationDecoder: BsonDocumentDecoder[Duration] = bson =>
    Duration(
      bson.require("value").as[Long],
      TimeUnit.valueOf(bson.require("unit").as[String].toUpperCase)
    )

  /** A given [[BsonEncoder]] for [[Duration]]. */
  given durationEncoder[D <: Duration]: BsonDocumentEncoder[D] = duration =>
    bson {
      "value" :: duration.length
      "unit" :: duration.unit.toString
    }
