package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PromoteDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PromoteDataCodecTest.{
  promote,
  promoteDocument,
  wrongPromoteDocument,
  given
}
import io.github.jahrim.chess.game.service.components.data.codecs.PromoteToDataCodec.given
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

class PromoteDataCodecTest extends CodecTest("PromoteData"):
  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = promoteDocument
    val value: PromoteData = document.as[PromoteData]
    assert(value == promote)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongPromoteDocument
      val value: PromoteData = document.as[PromoteData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = promote.asBson.asDocument
    assert(document.require("pawn").as[PieceData] == PieceDataCodecTest.piece)
    assert(document.require("to").as[PromoteToData] == PromoteToData.Queen)

object PromoteDataCodecTest:
  val promote: PromoteData = PromoteData(
    PieceDataCodecTest.piece,
    PromoteToData.Queen
  )
  val promoteDocument: BsonDocument = bson {
    "pawn" :: PieceDataCodecTest.piece
    "to" :: PromoteToData.Queen
  }
  val wrongPromoteDocument: BsonDocument = bson {
    "InvalidField" :: PieceDataCodecTest.piece
    "to" :: PromoteToData.Queen
  }
