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

import io.github.chess.engine.model.game.ChessGameHistory as LegacyChessGameHistory
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.chess.engine.model.pieces.Piece as LegacyPiece

/** The history of the [[LegacyMove LegacyMove]]s executed in a chess game. */
trait MoveHistory extends LegacyChessGameHistory:
  /**
   * @return all the [[LegacyMove LegacyMove]]s registered in this
   *         [[MoveHistory]] ordered by decreasing age (the first
   *         [[LegacyMove LegacyMove]] is the oldest, the last is
   *         the newest).
   */
  override def all: Seq[LegacyMove] = zipWithPieces.map(_._2)

  /**
   * @param p the specified [[LegacyPiece LegacyPiece]].
   * @return all the [[LegacyMove LegacyMove]]s registered for the
   *         specified [[LegacyPiece LegacyPiece]] in this [[MoveHistory]],
   *         ordered by decreasing age (the first [[LegacyMove LegacyMove]]
   *         is the oldest, the last is the newest)
   */
  override def ofPiece(p: LegacyPiece): Seq[LegacyMove] = zipWithPieces.filter(p eq _._1).map(_._2)

  /**
   * Saves the specified [[LegacyMove LegacyMove]] executed by the
   * specified [[LegacyPiece LegacyPiece]] in this [[MoveHistory]].
   *
   * @param piece the [[LegacyPiece LegacyPiece]] executing the move.
   * @param move  the [[LegacyMove LegacyMove]] executed.
   * @return a new updated [[MoveHistory]].
   */
  override def save(piece: LegacyPiece, move: LegacyMove): MoveHistory

  /**
   * Saves the specified [[LegacyMove LegacyMove]]s executed by the specified
   * [[LegacyPiece LegacyPiece]]s in this [[MoveHistory]].
   *
   * @param moves the specified [[LegacyMove LegacyMove]]s executed by the
   *              specified [[LegacyPiece LegacyPiece]]s.
   * @return a new updated [[MoveHistory]].
   */
  override def saveAll(moves: Iterable[(LegacyPiece, LegacyMove)]): MoveHistory =
    moves.foldLeft(this)((acc, historyEntry) => acc.save(historyEntry._1, historyEntry._2))

  /**
   * As [[all]], but each [[LegacyMove LegacyMove]] is zipped with the
   * [[LegacyPiece LegacyPiece]] that was moved.
   */
  def zipWithPieces: Seq[(LegacyPiece, LegacyMove)]

/** Companion object of [[MoveHistory]]. */
object MoveHistory:
  /** A [[MoveHistory]] with no [[LegacyMove LegacyMove]]s registered. */
  private val EmptyMoveHistory: MoveHistory = MoveHistory(Seq())

  /** @return a [[MoveHistory]] with no [[LegacyMove LegacyMove]]s registered. */
  def empty: MoveHistory = EmptyMoveHistory

  /**
   * @param moves the specified [[LegacyMove LegacyMove]]s.
   * @return a new [[MoveHistory]] with the specified
   *         [[LegacyMove LegacyMove]] registered.
   */
  def apply(moves: Iterable[(LegacyPiece, LegacyMove)]): MoveHistory =
    BasicMoveHistory(moves.toSeq)

  /** Basic implementation of a [[MoveHistory]]. */
  private case class BasicMoveHistory(
      private val moves: Seq[(LegacyPiece, LegacyMove)] = List.empty
  ) extends MoveHistory:
    override def zipWithPieces: Seq[(LegacyPiece, LegacyMove)] = moves
    override def save(p: LegacyPiece, m: LegacyMove): MoveHistory =
      BasicMoveHistory(this.moves :+ (p, m))
