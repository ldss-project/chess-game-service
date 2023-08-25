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
