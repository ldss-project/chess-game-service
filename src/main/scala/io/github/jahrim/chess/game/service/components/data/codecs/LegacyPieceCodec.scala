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
