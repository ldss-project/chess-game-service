package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.GameOverCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameOver
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonNull

/** A [[BsonCodecTest]] for [[GameOverCodec]]. */
class GameOverCodecTest extends BsonCodecTest(GameOverCodecTest)

/** Companion object of [[GameOverCodecTest]]. */
object GameOverCodecTest extends BsonCodecTest.BsonCodecData[GameOver]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[GameOver](
      name = "GameOver",
      sample = bson {
        "cause" :: LegacyGameOverCauseCodecTest.bsonSample
        "winner" :: LegacyPlayerCodecTest.bsonSample
      },
      expected = GameOver(
        cause = LegacyGameOverCauseCodecTest.dataSample,
        winner = Some(LegacyPlayerCodecTest.dataSample)
      )
    ),
    DecodeSample[GameOver](
      name = "GameOver with no winner",
      sample = bson {
        "cause" :: LegacyGameOverCauseCodecTest.bsonSample
        "winner" :: BsonNull.VALUE
      },
      expected = GameOver(
        cause = LegacyGameOverCauseCodecTest.dataSample,
        winner = None
      )
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[GameOver](
      name = "GameOver with no cause",
      sample = bson { "winner" :: LegacyPlayerCodecTest.bsonSample }
    )
  )
