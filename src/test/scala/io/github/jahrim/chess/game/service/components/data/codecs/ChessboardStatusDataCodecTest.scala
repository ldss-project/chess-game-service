package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  CastlingData,
  ChessboardStatusData,
  EnPassantData,
  MoveData,
  MoveTypeData,
  PieceData,
  PieceTypeData,
  PositionData
}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ChessboardStatusDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ChessboardStatusDataCodecTest.{
  chessboardStatus,
  chessboardStatusDocument,
  wrongChessboardStatusDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonDocument
import org.bson.conversions.Bson
import test.AbstractTest

class ChessboardStatusDataCodecTest extends CodecTest("ChessboardStatusData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = chessboardStatusDocument
    val value: ChessboardStatusData = document.as[ChessboardStatusData]
    assert(value == chessboardStatus)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongChessboardStatusDocument
      val value: ChessboardStatusData = document.as[ChessboardStatusData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = chessboardStatus.asBson.asDocument
    assert(
      document.require("pieces").as[Seq[PieceData]] == Seq(PieceDataCodecTest.piece)
    )
    assert(
      document.require("moves").as[Seq[MoveData]] == Seq(MoveDataCodecTest.move)
    )

object ChessboardStatusDataCodecTest:
  val chessboardStatus: ChessboardStatusData =
    ChessboardStatusData(
      Seq(PieceDataCodecTest.piece),
      Seq(MoveDataCodecTest.move)
    )
  val chessboardStatusDocument: BsonDocument =
    bson {
      "pieces" :* (PieceDataCodecTest.pieceDocument)
      "moves" :* (MoveDataCodecTest.moveDocument)
    }
  val wrongChessboardStatusDocument: BsonDocument =
    bson {
      "InvalidField" :* (PieceDataCodecTest.pieceDocument)
      "moves" :* (MoveDataCodecTest.moveDocument)
    }
