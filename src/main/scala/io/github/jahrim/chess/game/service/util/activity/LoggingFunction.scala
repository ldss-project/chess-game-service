package io.github.jahrim.chess.game.service.util.activity

/** A [[Function Function]] that accepts [[String]]s in order to log them. */
@FunctionalInterface
trait LoggingFunction extends Function[String, Unit]
