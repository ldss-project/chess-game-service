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
  Player as LegacyPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameConfiguration
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPlayerCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[LegacyPlayerCodec]]. */
class LegacyPlayerCodecTest extends BsonCodecTest(LegacyPlayerCodecTest)

/** Companion object of [[LegacyPlayerCodecTest]]. */
object LegacyPlayerCodecTest extends BsonCodecTest.BsonCodecData[LegacyPlayer]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyPlayer](
      name = "White player",
      sample = bson { "name" :: "PlayerName"; "team" :: LegacyTeam.WHITE },
      expected = LegacyWhitePlayer("PlayerName")
    ),
    DecodeSample[LegacyPlayer](
      name = "Black player",
      sample = bson { "name" :: "PlayerName"; "team" :: LegacyTeam.BLACK },
      expected = LegacyBlackPlayer("PlayerName")
    ),
    DecodeSample[LegacyPlayer](
      name = "White guest player",
      sample = bson { "team" :: LegacyTeam.WHITE },
      expected = LegacyWhitePlayer(GameConfiguration.GuestPlayerName)
    ),
    DecodeSample[LegacyPlayer](
      name = "Black guest player",
      sample = bson { "team" :: LegacyTeam.BLACK },
      expected = LegacyBlackPlayer(GameConfiguration.GuestPlayerName)
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyPlayer](
      name = "Player without team",
      sample = bson { "name" :: "PlayerName" }
    )
  )

  override def encodeSamples: EncodeSampleIterable =
    decodeSamples.take(2).map(_.asEncodeSample) ++ Seq(
      EncodeSample[LegacyPlayer](
        name = "White guest player",
        sample = LegacyWhitePlayer(GameConfiguration.GuestPlayerName),
        expected = bson { "name" :: "_"; "team" :: LegacyTeam.WHITE }
      ),
      EncodeSample[LegacyPlayer](
        name = "Black guest player",
        sample = LegacyBlackPlayer(GameConfiguration.GuestPlayerName),
        expected = bson { "name" :: "_"; "team" :: LegacyTeam.BLACK }
      )
    )
