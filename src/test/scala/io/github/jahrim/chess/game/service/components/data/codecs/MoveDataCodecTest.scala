package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  CastlingData,
  EnPassantData,
  MoveData,
  MoveTypeData,
  PositionData
}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.PieceData
import io.github.jahrim.chess.game.service.components.data.PieceTypeData
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

class MoveDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for MoveData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "type" :: "Capture"
        "from" :# {
          "file" :: "B"
          "rank" :: "_1"
        }
        "to" :# {
          "file" :: "C"
          "rank" :: "_2"
        }
        "castling" :# {
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
        "enPassant" :# {
          "opponentPawn" :# {
            "type" :: "Pawn"
            "position" :# {
              "file" :: "D"
              "rank" :: "_4"
            }
          }
        }
      }
      val value: MoveData = document.as[MoveData]

      assert(
        value == MoveData(
          MoveTypeData.Capture,
          PositionData(File.B, Rank._1),
          PositionData(File.C, Rank._2),
          Option(
            CastlingData(
              PositionData(File.A, Rank._1),
              PositionData(File.D, Rank._1)
            )
          ),
          Option(EnPassantData(PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4))))
        )
      )

    }

    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "type" :: "Capture"
          "from" :# {
            "file" :: "B"
            "rank" :: "_11"
          }
          "to" :# {
            "file" :: "C"
            "rank" :: "_2"
          }
          "castling" :# {
            "rook" :# {
              "from" :# {
                "file" :: "B"
                "rank" :: "_1"
              }
              "to" :# {
                "file" :: "C"
                "rank" :: "_2"
              }

            }
          }
          "enPassant" :# {
            "opponentPawn" :# {
              "type" :: "Pawn"
              "position" :# {
                "file" :: "B"
                "rank" :: "_1"
              }
            }
          }
        }
        val value: MoveData = document.as[MoveData]
      }
    }
  }

  describe("A BsonEncoder for MoveData") {
    it("should encode properly a move") {
      val value: MoveData = MoveData(
        MoveTypeData.Capture,
        PositionData(File.B, Rank._2),
        PositionData(File.C, Rank._3),
        Option(
          CastlingData(
            PositionData(File.C, Rank._3),
            PositionData(File.C, Rank._4)
          )
        ),
        Option(EnPassantData(PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4))))
      )
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("type").as[MoveTypeData] == MoveTypeData.Capture)
      assert(document.require("from").as[PositionData] == PositionData(File.B, Rank._2))
      assert(document.require("to").as[PositionData] == PositionData(File.C, Rank._3))
      assert(
        document.require("castling").as[CastlingData] == CastlingData(
          PositionData(File.C, Rank._3),
          PositionData(File.C, Rank._4)
        )
      )
      assert(
        document.require("enPassant").as[EnPassantData] == EnPassantData(
          PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4))
        )
      )

    }
  }

  after {}
