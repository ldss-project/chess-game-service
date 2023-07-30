package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{CastlingData, PositionData}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
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

class CastlingDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for CastlingData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "rook" :# {
          "from" :# {
            "file" :: "A"
            "rank" :: "_1"
          }
          "to" :# {
            "file" :: "D"
            "rank" :: "_1"
          }
        }
      }
      val value: CastlingData = document.as[CastlingData]

      assert(
        value ==
          CastlingData(
            PositionData(File.A, Rank._1),
            PositionData(File.D, Rank._1)
          )
      )
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "rook" :# {
            "from" :# {
              "file" :: "B"
              "rank" :: "_1"
            }
            "to" :# {
              "file" :: "D"
              "rank" :: "_12"
            }
          }
        }
        val value: CastlingData = document.as[CastlingData]
      }
    }
  }

  describe("A BsonEncoder for CastlingData") {
    it("should encode properly a castling") {
      val value: CastlingData =
        CastlingData(
          PositionData(File.A, Rank._1),
          PositionData(File.D, Rank._1)
        )

      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("rook.from").as[PositionData] == PositionData(File.A, Rank._1))
      assert(document.require("rook.to").as[PositionData] == PositionData(File.D, Rank._1))
    }
  }

  after {}
