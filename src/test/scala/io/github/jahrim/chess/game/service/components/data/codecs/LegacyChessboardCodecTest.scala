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

import io.github.chess.engine.model.board.ChessBoardBuilder.DSL.*
import io.github.chess.engine.model.board.{
  ChessBoard as LegacyChessboard,
  File as LegacyFile,
  Position as LegacyPosition,
  Rank as LegacyRank
}
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyChessboardCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPositionCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[LegacyChessboardCodec]]. */
class LegacyChessboardCodecTest extends BsonCodecTest(LegacyChessboardCodecTest)

/** Companion object of [[LegacyChessboardCodecTest]]. */
object LegacyChessboardCodecTest extends BsonCodecTest.BsonCodecData[LegacyChessboard]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyChessboard](
      name = "Chessboard",
      sample = bson {
        "pieces" :* (
          bson {
            "position" :: LegacyPosition(LegacyFile.A, LegacyRank._1)
            "piece" :: LegacyPieceCodecTest.bsonSample
          },
          bson {
            "position" :: LegacyPosition(LegacyFile.B, LegacyRank._1)
            "piece" :: LegacyPieceCodecTest.bsonSample
          }
        )
      },
      expected = LegacyChessboard { ** { 7 }; P | P | ** }
    ),
    DecodeSample[LegacyChessboard](
      name = "Empty Chessboard",
      sample = bson { "pieces" :: Seq() },
      expected = LegacyChessboard { ** { 8 } }
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyChessboard](
      name = "Chessboard with no positions",
      sample = bson {
        "pieces" :* (
          bson { "piece" :: LegacyPieceCodecTest.bsonSample },
          bson { "piece" :: LegacyPieceCodecTest.bsonSample }
        )
      }
    ),
    WrongDecodeSample[LegacyChessboard](
      name = "Chessboard with no pieces",
      sample = bson {
        "pieces" :* (
          bson { "position" :: LegacyPosition(LegacyFile.A, LegacyRank._1) },
          bson { "position" :: LegacyPosition(LegacyFile.B, LegacyRank._1) }
        )
      }
    )
  )
