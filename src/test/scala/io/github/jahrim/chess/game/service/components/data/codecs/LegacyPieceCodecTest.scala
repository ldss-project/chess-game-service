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

import io.github.chess.engine.model.pieces.{
  Bishop as LegacyBishop,
  King as LegacyKing,
  Knight as LegacyKnight,
  Pawn as LegacyPawn,
  Piece as LegacyPiece,
  Queen as LegacyQueen,
  Rook as LegacyRook
}
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPieceCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[LegacyPieceCodec]]. */
class LegacyPieceCodecTest extends BsonCodecTest(LegacyPieceCodecTest)

/** Companion object of [[LegacyPieceCodecTest]]. */
object LegacyPieceCodecTest extends BsonCodecTest.BsonCodecData[LegacyPiece]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyPiece](
      name = "Pawn",
      sample = bson { "type" :: "Pawn"; "team" :: LegacyTeamCodecTest.bsonSample },
      expected = LegacyPawn(LegacyTeamCodecTest.dataSample)
    ),
    DecodeSample[LegacyPiece](
      name = "Knight",
      sample = bson { "type" :: "Knight"; "team" :: LegacyTeamCodecTest.bsonSample },
      expected = LegacyKnight(LegacyTeamCodecTest.dataSample)
    ),
    DecodeSample[LegacyPiece](
      name = "Bishop",
      sample = bson { "type" :: "Bishop"; "team" :: LegacyTeamCodecTest.bsonSample },
      expected = LegacyBishop(LegacyTeamCodecTest.dataSample)
    ),
    DecodeSample[LegacyPiece](
      name = "Rook",
      sample = bson { "type" :: "Rook"; "team" :: LegacyTeamCodecTest.bsonSample },
      expected = LegacyRook(LegacyTeamCodecTest.dataSample)
    ),
    DecodeSample[LegacyPiece](
      name = "Queen",
      sample = bson { "type" :: "Queen"; "team" :: LegacyTeamCodecTest.bsonSample },
      expected = LegacyQueen(LegacyTeamCodecTest.dataSample)
    ),
    DecodeSample[LegacyPiece](
      name = "King",
      sample = bson { "type" :: "King"; "team" :: LegacyTeamCodecTest.bsonSample },
      expected = LegacyKing(LegacyTeamCodecTest.dataSample)
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyPiece](
      name = "Piece with unknown type",
      sample = bson { "type" :: "UnknownPieceType"; "team" :: LegacyTeamCodecTest.bsonSample }
    ),
    WrongDecodeSample[LegacyPiece](
      name = "Piece without type",
      sample = bson { "team" :: LegacyTeamCodecTest.bsonSample }
    ),
    WrongDecodeSample[LegacyPiece](
      name = "Piece without team",
      sample = bson { "type" :: "Pawn" }
    )
  )
