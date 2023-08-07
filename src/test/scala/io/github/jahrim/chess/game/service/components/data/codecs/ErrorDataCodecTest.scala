package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorDataCodecTest.{
  error,
  errorDocument,
  wrongErrorDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorTypeDataCodec.given
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

class ErrorDataCodecTest extends CodecTest("ErrorData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = errorDocument
    val value: ErrorData = document.as[ErrorData]
    assert(value == error)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongErrorDocument
      val value: ErrorData = document.as[ErrorData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = error.asBson.asDocument
    assert(document.require("type").as[ErrorTypeData] == ErrorTypeData.Generic)
    assert(document.require("message").as[String] == "generic message")

object ErrorDataCodecTest:
  val error: ErrorData = ErrorData(
    ErrorTypeData.Generic,
    "generic message"
  )
  val errorDocument: BsonDocument =
    bson {
      "type" :: "Generic"
      "message" :: "generic message"
    }
  val wrongErrorDocument: BsonDocument =
    bson {
      "type" :: "InvalidType"
      "message" :: "generic message"
    }
