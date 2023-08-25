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
