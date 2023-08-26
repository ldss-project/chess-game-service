package io.github.jahrim.chess.game.service.components.proxies.statistics.exceptions

/**
 * A [[StatisticsServiceException]] triggered when the user attempts to
 * call the api of this service with invalid input parameters.
 *
 * @param message a detailed description of the [[Exception]].
 */
class MalformedInputException(message: String) extends StatisticsServiceException(message)