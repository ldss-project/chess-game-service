package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{CastlingData, PositionData}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodecTest.{
  castling,
  castlingDocument,
  wrongCastlingDocument
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

class CastlingDataCodecTest extends CodecTest("CastlingData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = castlingDocument
    val value: CastlingData = document.as[CastlingData]
    assert(
      value == castling
    )

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongCastlingDocument
      val value: CastlingData = document.as[CastlingData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = castling.asBson.asDocument
    assert(document.require("rook.from").as[PositionData] == PositionDataCodecTest.position)
    assert(document.require("rook.to").as[PositionData] == PositionDataCodecTest.position)

object CastlingDataCodecTest:
  val castling: CastlingData = CastlingData(
    PositionDataCodecTest.position,
    PositionDataCodecTest.position
  )
  val castlingDocument: BsonDocument =
    bson {
      "rook" :: RookDataCodecTest.rookDocument
    }
  val wrongCastlingDocument: BsonDocument =
    bson {
      "rook" :: RookDataCodecTest.wrongRookDocument
    }
