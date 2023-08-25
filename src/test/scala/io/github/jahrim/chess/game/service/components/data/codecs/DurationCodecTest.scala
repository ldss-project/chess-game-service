package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

/** A [[BsonCodecTest]] for [[DurationCodec]]. */
class DurationCodecTest extends BsonCodecTest(DurationCodecTest)

/** Companion object of [[DurationCodecTest]]. */
object DurationCodecTest extends BsonCodecTest.BsonCodecData[Duration]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[Duration](
      name = "Duration",
      sample = bson { "value" :: 30L; "unit" :: "MINUTES" },
      expected = Duration(30, TimeUnit.MINUTES)
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[Duration](
      name = "Duration with wrong value",
      sample = bson { "value" :: 30d; "unit" :: "MINUTES" }
    ),
    WrongDecodeSample[Duration](
      name = "Duration with unknown unit",
      sample = bson { "value" :: 30L; "unit" :: "UnknownUnit" }
    )
  )
