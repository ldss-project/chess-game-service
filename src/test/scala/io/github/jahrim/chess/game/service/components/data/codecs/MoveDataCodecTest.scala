package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  CastlingData,
  EnPassantData,
  MoveData,
  MoveTypeData,
  PositionData
}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.PieceData
import io.github.jahrim.chess.game.service.components.data.PieceTypeData
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodecTest.{
  move,
  moveDocument,
  wrongMoveDocument
}
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

class MoveDataCodecTest extends CodecTest("MoveData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = moveDocument
    val value: MoveData = document.as[MoveData]
    assert(value == move)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongMoveDocument
      val value: MoveData = document.as[MoveData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = move.asBson.asDocument
    assert(document.require("type").as[MoveTypeData] == MoveTypeData.Capture)
    assert(document.require("from").as[PositionData] == PositionDataCodecTest.position)
    assert(document.require("to").as[PositionData] == PositionDataCodecTest.position)
    assert(document.require("castling").as[CastlingData] == CastlingDataCodecTest.castling)
    assert(document.require("enPassant").as[EnPassantData] == EnPassantDataCodecTest.enPassant)

object MoveDataCodecTest:
  val move: MoveData = MoveData(
    MoveTypeData.Capture,
    PositionDataCodecTest.position,
    PositionDataCodecTest.position,
    Option(CastlingDataCodecTest.castling),
    Option(EnPassantDataCodecTest.enPassant)
  )
  val moveDocument: BsonDocument = bson {
    "type" :: "Capture"
    "from" :: PositionDataCodecTest.positionDocument
    "to" :: PositionDataCodecTest.positionDocument
    "castling" :: CastlingDataCodecTest.castlingDocument
    "enPassant" :: EnPassantDataCodecTest.enPassantDocument
  }
  val wrongMoveDocument: BsonDocument = bson {
    "type" :: "InvalidType"
    "from" :: PositionDataCodecTest.positionDocument
    "to" :: PositionDataCodecTest.positionDocument
    "castling" :: CastlingDataCodecTest.castlingDocument
    "enPassant" :: EnPassantDataCodecTest.enPassantDocument
  }
