package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.chess.game.service.components.data.RookData
import io.github.jahrim.chess.game.service.components.data.codecs.RookDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RookDataCodecTest.{
  rook,
  rookDocument,
  wrongRookDocument
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

class RookDataCodecTest extends CodecTest("RookData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = rookDocument
    val value: RookData = document.as[RookData]
    assert(value == rook)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongRookDocument
      val value: RookData = document.as[RookData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = rook.asBson.asDocument
    assert(document.require("from").as[PositionData] == PositionDataCodecTest.position)
    assert(document.require("to").as[PositionData] == PositionDataCodecTest.position)

object RookDataCodecTest:
  val rook: RookData = RookData(PositionDataCodecTest.position, PositionDataCodecTest.position)
  val rookDocument: BsonDocument = bson {
    "from" :: PositionDataCodecTest.positionDocument
    "to" :: PositionDataCodecTest.positionDocument
  }
  val wrongRookDocument: BsonDocument = bson {
    "from" :: PositionDataCodecTest.wrongPositionDocument
    "to" :: PositionDataCodecTest.positionDocument
  }
