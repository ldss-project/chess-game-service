package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerStateDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
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

class ServerDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for ServerData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "state" :: "Running"
        "error" :# {
          "type" :: "Generic"
          "message" :: "generic error"
        }
      }
      val value: ServerData = document.as[ServerData]

      assert(
        value == ServerData(
          ServerStateData.Running,
          Option(ErrorData(ErrorTypeData.Generic, "generic error"))
        )
      )

    }

    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "state" :: "Running"
          "error" :# {
            "type" :: "some"
            "message" :: "generic error"
          }
        }
        val value: ServerData = document.as[ServerData]
      }
    }
  }

  describe("A BsonEncoder for ServerData") {
    it("should encode properly a server") {
      val value: ServerData = ServerData(
        ServerStateData.Running,
        Option(ErrorData(ErrorTypeData.Generic, "generic error"))
      )
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("state").as[ServerStateData] == ServerStateData.Running)
      assert(
        document("error").map(_.as[ErrorData]) == Option(
          ErrorData(ErrorTypeData.Generic, "generic error")
        )
      )

    }
  }

  after {}
