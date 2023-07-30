package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
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

class PositionDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for PositionData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "file" :: "A"
        "rank" :: "_1"
      }
      val value: PositionData = document.as[PositionData]

      assert(value == PositionData(File.A, Rank._1))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "file" :: "I"
          "rank" :: "_1"
        }
        val value: PositionData = document.as[PositionData]
      }
    }
  }

  describe("A BsonEncoder for PositionData") {
    it("should encode properly a position") {
      val value: PositionData = PositionData(File.A, Rank._1)
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("file").as[File] == File.A)
      assert(document.require("rank").as[Rank] == Rank._1)
    }
  }

  after {}
