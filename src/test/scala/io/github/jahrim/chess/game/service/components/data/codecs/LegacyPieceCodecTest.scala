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
