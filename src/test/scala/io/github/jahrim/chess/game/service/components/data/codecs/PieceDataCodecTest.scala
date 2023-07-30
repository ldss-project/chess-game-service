package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.chess.game.service.components.data.PieceData
import io.github.jahrim.chess.game.service.components.data.PieceTypeData
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
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

/** Template test. */
class PieceDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for PieceData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "type" :: "Knight"
        "position" :# {
          "file" :: "B"
          "rank" :: "_1"
        }
      }
      val value: PieceData = document.as[PieceData]

      assert(value == PieceData(PieceTypeData.Knight, PositionData(File.B, Rank._1)))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "type" :: "Bishop"
          "position" :# {
            "file" :: "A"
            "rank" :: "_10"
          }
        }
        val value: PieceData = document.as[PieceData]
      }
    }
  }

  describe("A BsonEncoder for PieceData") {
    it("should encode properly a piece") {
      val value: PieceData = PieceData(PieceTypeData.Knight, PositionData(File.B, Rank._1))
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("type").as[PieceTypeData] == PieceTypeData.Knight)
      assert(document.require("position").as[PositionData] == PositionData(File.B, Rank._1))
    }
  }

  after {}
