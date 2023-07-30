package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  EnPassantData,
  PieceData,
  PieceTypeData,
  PositionData
}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
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
class EnPassantDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for EnPassantData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "opponentPawn" :# {
          "type" :: "Pawn"
          "position" :# {
            "file" :: "D"
            "rank" :: "_4"
          }
        }
      }
      val value: EnPassantData = document.as[EnPassantData]

      assert(value == EnPassantData(PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4))))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "opponentPawn" :# {
            "type" :: "Pawn"
            "position" :# {
              "file" :: "K"
              "rank" :: "_4"
            }
          }
        }
        val value: EnPassantData = document.as[EnPassantData]
      }
    }
  }

  describe("A BsonEncoder for EnPassantData") {
    it("should encode properly an enPassant") {
      val value: EnPassantData =
        EnPassantData(PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4)))
      val document: BsonDocument = value.asBson.asDocument
      assert(
        document.require("opponentPawn").as[PieceData] == PieceData(
          PieceTypeData.Pawn,
          PositionData(File.D, Rank._4)
        )
      )
    }
  }

  after {}
