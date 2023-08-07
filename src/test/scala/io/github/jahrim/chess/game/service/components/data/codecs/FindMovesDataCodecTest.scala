package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.FindMovesDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FindMovesDataCodecTest.{
  findMoves,
  findMovesDocument,
  wrongFindMovesDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodecTest.{
  move,
  moveDocument,
  wrongMoveDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
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

class FindMovesDataCodecTest extends CodecTest("FindMovesData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = findMovesDocument
    val value: FindMovesData = document.as[FindMovesData]
    assert(value == findMoves)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongFindMovesDocument
      val value: FindMovesData = document.as[FindMovesData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = findMoves.asBson.asDocument
    assert(document.require("position").as[PositionData] == PositionDataCodecTest.position)

object FindMovesDataCodecTest:
  val findMoves: FindMovesData = FindMovesData(
    PositionDataCodecTest.position
  )
  val findMovesDocument: BsonDocument = bson {
    "position" :: PositionDataCodecTest.positionDocument
  }
  val wrongFindMovesDocument: BsonDocument = bson {
    "InvalidField" :: PositionDataCodecTest.positionDocument
  }
