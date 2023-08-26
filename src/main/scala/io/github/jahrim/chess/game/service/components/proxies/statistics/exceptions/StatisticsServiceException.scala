package io.github.jahrim.chess.game.service.components.proxies.statistics.exceptions

/**
 * An [[Exception]] triggered by the statistics service.
 *
 * @param message a detailed description of the [[Exception]].
 */
class StatisticsServiceException(message: String) extends Exception(message) with Equals:
  override def canEqual(that: Any): Boolean = that.isInstanceOf[StatisticsServiceException]
  override def equals(that: Any): Boolean = that match
    case other: StatisticsServiceException =>
      this.canEqual(other) && this.getMessage == other.getMessage
    case _ => false
  override def hashCode(): Int = 31 * getClass.getCanonicalName.## + getMessage.##
