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

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.chess.engine.model.pieces.{
  Bishop as LegacyBishop,
  King as LegacyKing,
  Knight as LegacyKnight,
  Pawn as LegacyPawn,
  Piece as LegacyPiece,
  Queen as LegacyQueen,
  Rook as LegacyRook
}
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.util.Try

/** [[Bson]] codec for [[LegacyPiece LegacyPiece]]. */
object LegacyPieceCodec:
  /** A given [[BsonDecoder]] for [[LegacyPiece LegacyPiece]]. */
  given legacyPieceDecoder: BsonDocumentDecoder[LegacyPiece] = bson =>
    val team = bson.require("team").as[LegacyTeam]
    pieceTypeDecoder.decode(bson.require("type")) match
      case PieceType.Pawn   => LegacyPawn(team)
      case PieceType.Knight => LegacyKnight(team)
      case PieceType.Bishop => LegacyBishop(team)
      case PieceType.Rook   => LegacyRook(team)
      case PieceType.Queen  => LegacyQueen(team)
      case PieceType.King   => LegacyKing(team)

  /** A given [[BsonEncoder]] for [[LegacyPiece LegacyPiece]]. */
  given legacyPieceEncoder[P <: LegacyPiece]: BsonDocumentEncoder[P] = piece =>
    bson {
      "type" :: pieceTypeEncoder.encode(PieceType.ofPiece(piece))
      "team" :: piece.team
    }

  /** A [[BsonDecoder]] for [[PieceType]]. */
  private def pieceTypeDecoder: BsonDecoder[PieceType] = bson =>
    PieceType.valueOf(bson.asString.getValue)

  /** A [[BsonEncoder]] for [[PieceType]]. */
  private def pieceTypeEncoder: BsonEncoder[PieceType] = pieceType => pieceType.toString.asBson

  /** A type of [[LegacyPiece LegacyPiece]]. */
  private enum PieceType:
    case Pawn, Knight, Bishop, Rook, Queen, King

  /** Companion object of [[PieceType]]. */
  private object PieceType:
    /**
     * @param piece the specified [[LegacyPiece LegacyPiece]].
     * @return the [[PieceType]] of the specified [[LegacyPiece LegacyPiece]].
     */
    def ofPiece(piece: LegacyPiece): PieceType =
      val pieceTypeName: String = piece.getClass.getSimpleName
      Try(PieceType.valueOf(pieceTypeName)).getOrElse(
        throw new IllegalArgumentException(
          s"Name '$pieceTypeName' does not refer to any valid type for a concrete chess piece."
        )
      )
