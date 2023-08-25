package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.TimeConstraint as LegacyTimeConstraint
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.TimeConstraint
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

/** [[Bson]] codec for [[LegacyTimeConstraint LegacyTimeConstraint]]. */
object LegacyTimeConstraintCodec:
  /** A given [[BsonDecoder]] for [[LegacyTimeConstraint LegacyTimeConstraint]]. */
  given legacyTimeConstraintDecoder: BsonDocumentDecoder[LegacyTimeConstraint] = bson =>
    val constraint = TimeConstraint.valueOf(bson.require("type").as[String])
    bson("time")
      .flatMap(_.as[Option[Duration]])
      .map(duration => constraint.withDuration(duration.toMinutes.toInt))
      .getOrElse(constraint)
      .legacy

  /** A given [[BsonEncoder]] for [[LegacyTimeConstraint LegacyTimeConstraint]]. */
  given timeConstraintEncoder: BsonDocumentEncoder[LegacyTimeConstraint] = legacyTimeConstraint =>
    bson {
      "type" :: TimeConstraint.fromLegacy(legacyTimeConstraint).toString
      "time" :: (
        legacyTimeConstraint match
          case LegacyTimeConstraint.NoLimit => None
          case _ => Some(Duration(legacyTimeConstraint.minutes, TimeUnit.MINUTES))
      )
    }
