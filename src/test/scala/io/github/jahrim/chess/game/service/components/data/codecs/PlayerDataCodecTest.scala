package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PlayerData
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerDataCodec.given
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

class PlayerDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for PlayerData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "username" :: "freddiemerc"
      }
      val value: PlayerData = document.as[PlayerData]

      assert(value == PlayerData("freddiemerc"))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "file" :: "A"
        }
        val value: PlayerData = document.as[PlayerData]
      }
    }
  }

  describe("A BsonEncoder for PlayerData") {
    it("should encode properly a player") {
      val value: PlayerData = PlayerData("freddiemerc")
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("username").as[String] == "freddiemerc")
    }
  }

  after {}
