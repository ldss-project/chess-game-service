package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.{codecs, *}
import io.github.jahrim.chess.game.service.components.data.codecs.ApplyMoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PromoteDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FindMovesDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.InputDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.InputDataCodecTest.{
  input,
  inputDocument,
  wrongInputDocument
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

class InputDataCodecTest extends CodecTest("InputData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = inputDocument
    val value: InputData = document.as[InputData]
    assert(value == input)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongInputDocument
      val value: InputData = document.as[InputData]
      println(value)
      println(wrongInputDocument)
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = input.asBson.asDocument
    assert(document.require("findMoves").as[FindMovesData] == FindMovesDataCodecTest.findMoves)
    assert(document.require("applyMove").as[ApplyMoveData] == ApplyMoveDataCodecTest.applyMove)
    assert(document.require("promote").as[PromoteData] == PromoteDataCodecTest.promote)

object InputDataCodecTest:
  val input: InputData = InputData(
    Option(FindMovesDataCodecTest.findMoves),
    Option(ApplyMoveDataCodecTest.applyMove),
    Option(PromoteDataCodecTest.promote)
  )
  val inputDocument: BsonDocument = bson {
    "findMoves" :: FindMovesDataCodecTest.findMovesDocument
    "applyMove" :: ApplyMoveDataCodecTest.applyMoveDocument
    "promote" :: PromoteDataCodecTest.promoteDocument
  }
  val wrongInputDocument: BsonDocument = bson {
    "findMoves" :: FindMovesDataCodecTest.wrongFindMovesDocument
    "applyMove" :: ApplyMoveDataCodecTest.applyMoveDocument
    "promote" :: PromoteDataCodecTest.promoteDocument
  }
