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
import io.github.jahrim.chess.game.service.components.data.codecs.ChessGameServiceExceptionCodec.given
import io.github.jahrim.chess.game.service.components.exceptions.{
  ChessGameServiceException,
  GameNotRunningException
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[ChessGameServiceExceptionCodec]]. */
class ChessGameServiceExceptionCodecTest extends BsonCodecTest(ChessGameServiceExceptionCodecTest)

/** Companion object of [[ChessGameServiceExceptionCodecTest]]. */
object ChessGameServiceExceptionCodecTest
    extends BsonCodecTest.BsonCodecData[ChessGameServiceException]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[ChessGameServiceException](
      name = "ChessGameServiceException",
      sample = bson { "type" :: "ChessGameServiceException"; "message" :: "ErrorMessage" },
      expected = ChessGameServiceException("ErrorMessage")
    ),
    DecodeSample[ChessGameServiceException](
      name = "Specific ChessGameServiceException",
      sample = bson {
        "type" :: "GameNotRunningException";
        "message" :: "Game 'ID' rejected the request: not running."
      },
      expected = GameNotRunningException("ID")
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[ChessGameServiceException](
      name = "Unknown exception",
      sample = bson { "type" :: "UnknownException"; "message" :: "ErrorMessage" }
    )
  )
