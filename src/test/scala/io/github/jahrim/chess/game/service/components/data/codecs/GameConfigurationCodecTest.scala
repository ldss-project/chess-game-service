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
