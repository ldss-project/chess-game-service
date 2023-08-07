package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  EnPassantData,
  PieceData,
  PieceTypeData,
  PositionData
}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodecTest.{
  enPassant,
  enPassantDocument,
  wrongEnPassantDocument
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

class EnPassantDataCodecTest extends CodecTest("EnPassantData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = enPassantDocument
    val value: EnPassantData = document.as[EnPassantData]
    assert(value == enPassant)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongEnPassantDocument
      val value: EnPassantData = document.as[EnPassantData]
    }

  override def encodeTest(): Unit =
    val value: EnPassantData = enPassant
    val document: BsonDocument = value.asBson.asDocument
    assert(
      document.require("opponentPawn").as[PieceData] == PieceDataCodecTest.piece
    )

object EnPassantDataCodecTest:
  val enPassant: EnPassantData = EnPassantData(PieceDataCodecTest.piece)
  val enPassantDocument: BsonDocument = bson {
    "opponentPawn" :: PieceDataCodecTest.pieceDocument
  }
  val wrongEnPassantDocument: BsonDocument = bson {
    "opponentPawn" :: PieceDataCodecTest.wrongPieceDocument
  }
