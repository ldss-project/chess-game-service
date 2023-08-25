package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPositionCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[LegacyPositionCodec]]. */
class LegacyPositionCodecTest extends BsonCodecTest(LegacyPositionCodecTest)

/** Companion object of [[LegacyPositionCodecTest]]. */
object LegacyPositionCodecTest extends BsonCodecTest.BsonCodecData[LegacyPosition]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyPosition](
      name = "Position",
      sample = bson {
        "file" :: LegacyFileCodecTest.bsonSample
        "rank" :: LegacyRankCodecTest.bsonSample
      },
      expected = LegacyPosition(
        LegacyFileCodecTest.dataSample,
        LegacyRankCodecTest.dataSample
      )
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyPosition](
      name = "Position with missing file",
      sample = bson { "rank" :: LegacyRankCodecTest.bsonSample }
    ),
    WrongDecodeSample[LegacyPosition](
      name = "Position with missing rank",
      sample = bson { "file" :: LegacyFileCodecTest.bsonSample }
    )
  )
