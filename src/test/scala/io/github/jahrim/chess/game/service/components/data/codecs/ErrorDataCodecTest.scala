package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorDataCodec.given
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

class ErrorDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for ErrorData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "type" :: "Generic"
        "message" :: "generic message"
      }
      val value: ErrorData = document.as[ErrorData]

      assert(
        value == ErrorData(
          ErrorTypeData.Generic,
          "generic message"
        )
      )

    }

    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "message" :: "generic message"
        }
        val value: ErrorData = document.as[ErrorData]
      }
    }
  }

  describe("A BsonEncoder for ErrorData") {
    it("should encode properly a error") {
      val value: ErrorData = ErrorData(
        ErrorTypeData.Generic,
        "generic message"
      )
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("type").as[ErrorTypeData] == ErrorTypeData.Generic)
      assert(document.require("message").as[String] == "generic message")

    }
  }

  after {}
