package io.github.jahrim.chess.game.service.components.proxies.statistics.exceptions

/**
 * A [[StatisticsServiceException]] triggered when a user is queried
 * but not found.
 *
 * @param message a detailed description of the [[Exception]].
 */
class UserNotFoundException(message: String) extends StatisticsServiceException(message)

/** Companion object of [[UserNotFoundException]]. */
object UserNotFoundException:
  def apply(username: String): UserNotFoundException =
    new UserNotFoundException(s"User '$username' not found.")
