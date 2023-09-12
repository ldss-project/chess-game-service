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
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTimeConstraintCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.TimeConstraint
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.{BsonNull, BsonString}

/** A [[BsonCodecTest]] for [[LegacyTimeConstraintCodec]]. */
class LegacyTimeConstraintCodecTest extends BsonCodecTest(LegacyTimeConstraintCodecTest)

/** Companion object of [[LegacyTimeConstraintCodecTest]]. */
object LegacyTimeConstraintCodecTest extends BsonCodecTest.BsonCodecData[LegacyTimeConstraint]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyTimeConstraint](
      name = "NoLimit",
      sample = bson { "type" :: "NoLimit"; "time" :: BsonNull.VALUE },
      expected = LegacyTimeConstraint.NoLimit
    ),
    DecodeSample[LegacyTimeConstraint](
      name = "PlayerLimit",
      sample = bson { "type" :: "PlayerLimit"; "time" :: DurationCodecTest.bsonSample },
      expected =
        TimeConstraint.PlayerLimit.withDuration(DurationCodecTest.dataSample.toMinutes.toInt).legacy
    ),
    DecodeSample[LegacyTimeConstraint](
      name = "MoveLimit",
      sample = bson { "type" :: "MoveLimit"; "time" :: DurationCodecTest.bsonSample },
      expected =
        TimeConstraint.MoveLimit.withDuration(DurationCodecTest.dataSample.toMinutes.toInt).legacy
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyTimeConstraint](
      name = "Unknown TimeConstraint",
      sample = bson { "type" :: "UnknownLegacyTimeConstraint" }
    )
  )
