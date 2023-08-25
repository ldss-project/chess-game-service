package io.github.jahrim.chess.game.service.components.exceptions

/**
 * A [[ChessGameServiceException]] triggered by an internal error,
 * probably due to implementation, during the execution of the chess
 * game service.
 *
 * @param message a detailed description of the [[Exception]].
 */
class InternalServerException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[InternalServerException]]. */
object InternalServerException:
  def apply(cause: Throwable): InternalServerException =
    new InternalServerException(s"${cause.getClass.getSimpleName}: ${cause.getMessage}")
