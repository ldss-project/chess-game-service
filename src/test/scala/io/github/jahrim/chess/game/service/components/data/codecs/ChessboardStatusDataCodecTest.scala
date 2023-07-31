package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  PieceData,
  PieceTypeData,
  PositionData,
  ChessboardStatusData,
  MoveData,
  MoveTypeData,
  CastlingData,
  EnPassantData
}
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ChessboardStatusDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
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

class ChessboardStatusDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for ChessboardStatusData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "pieces" :* (
          bson {
            "type" :: "Knight"
            "position" :# {
              "file" :: "B"
              "rank" :: "_1"
            }
          }
        )
        "moves" :* (
          bson {
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
        )
      }
      val value: ChessboardStatusData = document.as[ChessboardStatusData]
      println(value)
      assert(
        value ==
          ChessboardStatusData(
            Seq(PieceData(PieceTypeData.Knight, PositionData(File.B, Rank._1))),
            Seq(
              MoveData(
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
          )
      )
      println(ChessboardStatusData)
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "pieces" :* (
            bson {
              "type" :: "Knight"
              "position" :# {
                "file" :: "B"
                "rank" :: "_1"
              }
            }
          )
          "moves" :* (
            bson {
              "type" :: "Capture"
              "from" :# {
                "file" :: "B"
                "rank" :: "_13"
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
          )
        }
        val value: ChessboardStatusData = document.as[ChessboardStatusData]
      }
    }
  }

  describe("A BsonEncoder for ChessboardStatusData") {
    it("should encode properly a chessboard") {
      val value: ChessboardStatusData = ChessboardStatusData(
        Seq(PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4))),
        Seq(
          MoveData(
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
      )
      val document: BsonDocument = value.asBson.asDocument
      assert(
        document.require("pieces").as[Seq[PieceData]] == Seq(
          PieceData(PieceTypeData.Pawn, PositionData(File.D, Rank._4))
        )
      )
      assert(
        document.require("moves").as[Seq[MoveData]] == Seq(
          MoveData(
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
      )
    }
  }

  after {}
