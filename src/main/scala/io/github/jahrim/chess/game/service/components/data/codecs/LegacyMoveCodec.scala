/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele, Kentpayeva Madina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.moves.{
  CaptureMove as LegacyCaptureMove,
  CastlingMove as LegacyCastlingMove,
  DoubleMove as LegacyDoubleMove,
  EnPassantMove as LegacyEnPassantMove,
  Move as LegacyMove
}
import io.github.chess.engine.model.pieces.Piece as LegacyPiece
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPieceCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPositionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.moves.BasicDoubleMove
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyMove LegacyMove]]. */
object LegacyMoveCodec:
  /** A given [[BsonDecoder]] for [[LegacyMove LegacyMove]]. */
  given legacyMoveDecoder: BsonDocumentDecoder[LegacyMove] = bson =>
    val from = bson.require("from").as[LegacyPosition]
    val to = bson.require("to").as[LegacyPosition]
    None
      .orElse(
        bson("castling")
          .map(_.asDocument)
          .map(castling =>
            LegacyCastlingMove(
              from,
              to,
              castling.require("rookFrom").as[LegacyPosition],
              castling.require("rookTo").as[LegacyPosition]
            )
          )
      )
      .orElse(
        bson("doubleMove")
          .map(_.asDocument)
          .map(doubleMove =>
            BasicDoubleMove(
              from,
              to,
              doubleMove.require("passingBy").as[LegacyPosition]
            )
          )
      )
      .orElse(
        bson("enPassant")
          .map(_.asDocument)
          .map(enPassantMove =>
            LegacyEnPassantMove(
              from,
              to,
              capturedPawn = bson.require("capturedPiece").as[LegacyPiece],
              capturedPawnPosition =
                enPassantMove.require("capturedPawnPosition").as[LegacyPosition]
            )
          )
      )
      .orElse(
        bson("capturedPiece")
          .map(_.as[LegacyPiece])
          .map(capturedPiece =>
            LegacyCaptureMove(
              from,
              to,
              capturedPiece
            )
          )
      )
      .getOrElse(
        LegacyMove(from, to)
      )

  /** A given [[BsonEncoder]] for [[LegacyMove LegacyMove]]. */
  given legacyMoveEncoder[M <: LegacyMove]: BsonDocumentEncoder[M] = legacyMove =>
    bson {
      "from" :: legacyMove.from
      "to" :: legacyMove.to
      legacyMove match
        case legacyCastlingMove: LegacyCastlingMove =>
          "castling" :# {
            "rookFrom" :: legacyCastlingMove.rookFromPosition
            "rookTo" :: legacyCastlingMove.rookToPosition
          }
        case legacyDoubleMove: LegacyDoubleMove =>
          "doubleMove" :# {
            "passingBy" :: legacyDoubleMove.middlePosition
          }
        case legacyEnPassantMove: LegacyEnPassantMove =>
          "capturedPiece" :: legacyEnPassantMove.capturedPiece
          "enPassant" :# {
            "capturedPawnPosition" :: legacyEnPassantMove.capturedPiecePosition
          }
        case legacyCaptureMove: LegacyCaptureMove =>
          "capturedPiece" :: legacyCaptureMove.capturedPiece
        case _ =>
    }
