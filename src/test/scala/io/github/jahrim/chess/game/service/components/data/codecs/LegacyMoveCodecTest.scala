package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{
  File as LegacyFile,
  Position as LegacyPosition,
  Rank as LegacyRank
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.chess.engine.model.moves.{
  CaptureMove as LegacyCaptureMove,
  CastlingMove as LegacyCastlingMove,
  DoubleMove as LegacyDoubleMove,
  EnPassantMove as LegacyEnPassant,
  Move as LegacyMove
}
import io.github.chess.engine.model.pieces.Pawn as LegacyPawn
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyMoveCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPieceCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPositionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.moves.BasicDoubleMove
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[LegacyMoveCodec]]. */
class LegacyMoveCodecTest extends BsonCodecTest(LegacyMoveCodecTest)

/** Companion object of [[LegacyMoveCodecTest]]. */
object LegacyMoveCodecTest extends BsonCodecTest.BsonCodecData[LegacyMove]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyMove](
      name = "Move",
      sample = bson {
        "from" :: LegacyPosition(LegacyFile.A, LegacyRank._2)
        "to" :: LegacyPosition(LegacyFile.A, LegacyRank._3)
      },
      expected = LegacyMove(
        from = LegacyPosition(LegacyFile.A, LegacyRank._2),
        to = LegacyPosition(LegacyFile.A, LegacyRank._3)
      )
    ),
    DecodeSample[LegacyMove](
      name = "DoubleMove",
      sample = bson {
        "from" :: LegacyPosition(LegacyFile.A, LegacyRank._2)
        "to" :: LegacyPosition(LegacyFile.A, LegacyRank._4)
        "doubleMove" :# {
          "passingBy" :: LegacyPosition(LegacyFile.A, LegacyRank._3)
        }
      },
      expected = BasicDoubleMove(
        from = LegacyPosition(LegacyFile.A, LegacyRank._2),
        to = LegacyPosition(LegacyFile.A, LegacyRank._4),
        middlePosition = LegacyPosition(LegacyFile.A, LegacyRank._3)
      )
    ),
    DecodeSample[LegacyMove](
      name = "CastlingMove",
      sample = bson {
        "from" :: LegacyPosition(LegacyFile.E, LegacyRank._1)
        "to" :: LegacyPosition(LegacyFile.C, LegacyRank._1)
        "castling" :# {
          "rookFrom" :: LegacyPosition(LegacyFile.A, LegacyRank._1)
          "rookTo" :: LegacyPosition(LegacyFile.D, LegacyRank._1)
        }
      },
      expected = LegacyCastlingMove(
        kingFrom = LegacyPosition(LegacyFile.E, LegacyRank._1),
        kingTo = LegacyPosition(LegacyFile.C, LegacyRank._1),
        rookFromPosition = LegacyPosition(LegacyFile.A, LegacyRank._1),
        rookToPosition = LegacyPosition(LegacyFile.D, LegacyRank._1)
      )
    ),
    DecodeSample[LegacyMove](
      name = "CaptureMove",
      sample = bson {
        "from" :: LegacyPosition(LegacyFile.E, LegacyRank._1)
        "to" :: LegacyPosition(LegacyFile.D, LegacyRank._1)
        "capturedPiece" :: LegacyPieceCodecTest.bsonSample
      },
      expected = LegacyCaptureMove(
        from = LegacyPosition(LegacyFile.E, LegacyRank._1),
        to = LegacyPosition(LegacyFile.D, LegacyRank._1),
        capturedPiece = LegacyPieceCodecTest.dataSample
      )
    ),
    DecodeSample[LegacyMove](
      name = "EnPassantMove",
      sample = bson {
        "from" :: LegacyPosition(LegacyFile.B, LegacyRank._4)
        "to" :: LegacyPosition(LegacyFile.A, LegacyRank._3)
        "capturedPiece" :: LegacyPawn(LegacyTeam.WHITE)
        "enPassant" :# {
          "capturedPawnPosition" :: LegacyPosition(LegacyFile.A, LegacyRank._4)
        }
      },
      expected = LegacyEnPassant(
        from = LegacyPosition(LegacyFile.B, LegacyRank._4),
        to = LegacyPosition(LegacyFile.A, LegacyRank._3),
        capturedPawnPosition = LegacyPosition(LegacyFile.A, LegacyRank._4),
        capturedPawn = LegacyPawn(LegacyTeam.WHITE)
      )
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyMove](
      name = "Move with no starting position",
      sample = bson { "to" :: LegacyPositionCodecTest.bsonSample }
    ),
    WrongDecodeSample[LegacyMove](
      name = "Move with no arriving position",
      sample = bson { "from" :: LegacyPositionCodecTest.bsonSample }
    )
  )
