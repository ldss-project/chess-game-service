package io.github.jahrim.chess.game.service.components.exceptions

/**
 * An [[Exception]] triggered by the chess game service.
 *
 * @param message a detailed description of the [[Exception]].
 */
class ChessGameServiceException(message: String) extends Exception(message) with Equals:
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ChessGameServiceException]
  override def equals(that: Any): Boolean = that match
    case other: ChessGameServiceException =>
      this.canEqual(other) && this.getMessage == other.getMessage
    case _ => false
  override def hashCode(): Int = 31 * getClass.getCanonicalName.## + getMessage.##
