package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.{BsonNull, BsonString}

import scala.concurrent.duration.Duration

/** A [[BsonCodecTest]] for [[OptionCodec]]. */
class OptionCodecTest extends BsonCodecTest(OptionCodecTest)

/** Companion object of [[OptionCodecTest]]. */
object OptionCodecTest extends BsonCodecTest.BsonCodecData[Option[?]]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[Option[String]](
      name = "Some of value",
      sample = BsonString("Value"),
      expected = Some("Value")
    ),
    DecodeSample[Option[Duration]](
      name = "Some of document",
      sample = DurationCodecTest.bsonSample,
      expected = Some(DurationCodecTest.dataSample)
    ),
    DecodeSample[Option[Duration]](
      name = "None",
      sample = BsonNull.VALUE,
      expected = None
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq()
