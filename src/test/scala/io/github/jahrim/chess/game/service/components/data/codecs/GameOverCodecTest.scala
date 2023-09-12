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
