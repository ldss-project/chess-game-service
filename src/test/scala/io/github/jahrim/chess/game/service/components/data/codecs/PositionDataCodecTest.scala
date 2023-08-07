package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodecTest.{
  position,
  positionDocument,
  wrongPositionDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
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

class PositionDataCodecTest extends CodecTest("PositionData"):
  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = positionDocument
    val value: PositionData = document.as[PositionData]
    assert(value == position)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] { wrongPositionDocument.as[PositionData] }

  override def encodeTest(): Unit =
    val document: BsonDocument = position.asBson.asDocument
    assert(document.require("file").as[File] == position.file)
    assert(document.require("rank").as[Rank] == position.rank)

object PositionDataCodecTest:
  val position: PositionData = PositionData(File.A, Rank._1)
  val positionDocument: BsonDocument = bson {
    "file" :: "A"
    "rank" :: "_1"
  }
  val wrongPositionDocument: BsonDocument = bson {
    "file" :: "InvalidFile"
    "rank" :: "_1"
  }
