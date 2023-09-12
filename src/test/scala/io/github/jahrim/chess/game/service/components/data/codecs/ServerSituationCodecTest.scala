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
import io.github.jahrim.chess.game.service.components.data.codecs.ServerSituationCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[ServerSituationCodec]]. */
class ServerSituationCodecTest extends BsonCodecTest(ServerSituationCodecTest)

/** Companion object of [[ServerSituationCodecTest]]. */
object ServerSituationCodecTest extends BsonCodecTest.BsonCodecData[ServerSituation]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[ServerSituation](
      name = "NotConfigured",
      sample = BsonString("NotConfigured"),
      expected = ServerSituation.NotConfigured
    ),
    DecodeSample[ServerSituation](
      name = "WaitingForPlayers",
      sample = BsonString("WaitingForPlayers"),
      expected = ServerSituation.WaitingForPlayers
    ),
    DecodeSample[ServerSituation](
      name = "Ready",
      sample = BsonString("Ready"),
      expected = ServerSituation.Ready
    ),
    DecodeSample[ServerSituation](
      name = "Running",
      sample = BsonString("Running"),
      expected = ServerSituation.Running
    ),
    DecodeSample[ServerSituation](
      name = "Terminated",
      sample = BsonString("Terminated"),
      expected = ServerSituation.Terminated
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[ServerSituation](
      name = "Unknown ServerSituation",
      sample = BsonString("UnknownServerSituation")
    )
  )
