package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.chess.game.service.components.data.RookData
import io.github.jahrim.chess.game.service.components.data.codecs.RookDataCodec.given
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

class RookDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for RookData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "from" :# {
          "file" :: "A"
          "rank" :: "_1"
        }
        "to" :# {
          "file" :: "B"
          "rank" :: "_2"
        }
      }
      val value: RookData = document.as[RookData]

      assert(value == RookData(PositionData(File.A, Rank._1), PositionData(File.B, Rank._2)))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "from" :# {
            "file" :: "A"
            "rank" :: "_1"
          }
        }
        val value: RookData = document.as[RookData]
      }
    }
  }

  describe("A BsonEncoder for RookData") {
    it("should encode properly a rookData") {
      val value: RookData = RookData(
        PositionData(File.A, Rank._1),
        PositionData(File.B, Rank._2)
      )
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("from").as[PositionData] == PositionData(File.A, Rank._1))
      assert(document.require("to").as[PositionData] == PositionData(File.B, Rank._2))
    }
  }

  after {}
