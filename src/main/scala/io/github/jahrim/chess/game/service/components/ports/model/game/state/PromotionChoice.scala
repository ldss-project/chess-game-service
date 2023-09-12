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
