package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.chess.engine.model.pieces.{
  Bishop as LegacyBishop,
  Knight as LegacyKnight,
  Piece as LegacyPiece,
  Queen as LegacyQueen,
  Rook as LegacyRook
}

/** One of the possible choices when promoting a pawn in a chess game. */
enum PromotionChoice:
  /** A [[PromotionChoice]] that will promote the pawn to a [[LegacyKnight LegacyKnight]]. */
  case Knight

  /** A [[PromotionChoice]] that will promote the pawn to a [[LegacyBishop LegacyBishop]]. */
  case Bishop

  /** A [[PromotionChoice]] that will promote the pawn to a [[LegacyRook LegacyRook]]. */
  case Rook

  /** A [[PromotionChoice]] that will promote the pawn to a [[LegacyQueen LegacyQueen]]. */
  case Queen

  /**
   * Build a [[LegacyPiece LegacyPiece]] of the specified [[LegacyTeam LegacyTeam]]
   * corresponding to this [[PromotionChoice]].
   *
   * @param team the specified [[LegacyTeam LegacyTeam]].
   * @return a [[LegacyPiece LegacyPiece]] of the specified [[LegacyTeam LegacyTeam]]
   *         corresponding to this [[PromotionChoice]].
   */
  def asPieceOfTeam(team: LegacyTeam): LegacyPiece = this match
    case PromotionChoice.Knight => LegacyKnight(team)
    case PromotionChoice.Bishop => LegacyBishop(team)
    case PromotionChoice.Rook   => LegacyRook(team)
    case PromotionChoice.Queen  => LegacyQueen(team)
