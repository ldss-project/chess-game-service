/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele, Kentpayeva Madina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
