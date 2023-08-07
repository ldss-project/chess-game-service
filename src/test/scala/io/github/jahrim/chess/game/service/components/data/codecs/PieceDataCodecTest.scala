package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.chess.game.service.components.data.PieceData
import io.github.jahrim.chess.game.service.components.data.PieceTypeData
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodecTest.{
  piece,
  pieceDocument,
  wrongPieceDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
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

class PieceDataCodecTest extends CodecTest("PieceData"):
  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = pieceDocument
    val value: PieceData = document.as[PieceData]
    assert(value == piece)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongPieceDocument
      val value: PieceData = document.as[PieceData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = piece.asBson.asDocument
    assert(document.require("type").as[PieceTypeData] == PieceTypeData.Pawn)
    assert(document.require("position").as[PositionData] == PositionDataCodecTest.position)

object PieceDataCodecTest:
  val piece: PieceData = PieceData(PieceTypeData.Pawn, PositionDataCodecTest.position)
  val pieceDocument: BsonDocument = bson {
    "type" :: "Pawn"
    "position" :: PositionDataCodecTest.positionDocument
  }
  val wrongPieceDocument: BsonDocument = bson {
    "type" :: "InvalidType"
    "position" :: PositionDataCodecTest.positionDocument
  }
