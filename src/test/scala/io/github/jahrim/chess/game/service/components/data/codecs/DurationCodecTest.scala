package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonDocument
import org.bson.conversions.Bson
import scala.concurrent.duration.Duration
import test.AbstractTest

class DurationCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for Duration") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "value" :: 2L
        "unit" :: "minutes"
      }
      val value: Duration = document.as[Duration]

      assert(value == Duration(2L, "minutes"))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "file" :: "I"
        }
        val value: Duration = document.as[Duration]
      }
    }
  }

  describe("A BsonEncoder for Duration") {
    it("should encode properly a duration") {
      val value: Duration = Duration(2L, "minutes")
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("value").as[Long] == 2L)
      assert(document.require("unit").as[String] == "MINUTES")
    }
  }

  after {}
