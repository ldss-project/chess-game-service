package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.GameConfigurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyGameModeCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPlayerCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTimeConstraintCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameConfiguration
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[GameConfigurationCodec]]. */
class GameConfigurationCodecTest extends BsonCodecTest(GameConfigurationCodecTest)

/** Companion object of [[GameConfigurationCodecTest]]. */
object GameConfigurationCodecTest extends BsonCodecTest.BsonCodecData[GameConfiguration]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[GameConfiguration](
      name = "GameConfiguration",
      sample = bson {
        "timeConstraint" :: LegacyTimeConstraintCodecTest.bsonSample
        "gameMode" :: LegacyGameModeCodecTest.bsonSample
        "whitePlayer" :: LegacyWhitePlayer("WhitePlayer")
        "blackPlayer" :: LegacyBlackPlayer("BlackPlayer")
        "gameId" :: "GameId"
        "isPrivate" :: true
      },
      expected = GameConfiguration(
        timeConstraint = LegacyTimeConstraintCodecTest.dataSample,
        gameMode = LegacyGameModeCodecTest.dataSample,
        whitePlayer = LegacyWhitePlayer("WhitePlayer"),
        blackPlayer = LegacyBlackPlayer("BlackPlayer"),
        gameId = "GameId",
        isPrivate = true
      )
    ),
    DecodeSample[GameConfiguration](
      name = "Default GameConfiguration",
      sample = emptyBson,
      expected = GameConfiguration.default
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq()

  override def encodeSamples: EncodeSampleIterable = Seq(
    decodeSamples.toSeq.head.asEncodeSample,
    EncodeSample[GameConfiguration](
      name = "Default GameConfiguration",
      sample = GameConfiguration.default,
      expected = bson {
        "timeConstraint" :: GameConfiguration.default.timeConstraint
        "gameMode" :: GameConfiguration.default.gameMode
        "whitePlayer" :: GameConfiguration.default.whitePlayerOption
        "blackPlayer" :: GameConfiguration.default.blackPlayerOption
        "gameId" :: GameConfiguration.default.gameIdOption
        "isPrivate" :: GameConfiguration.default.isPrivate
      }
    )
  )
