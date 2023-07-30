package io.github.jahrim.chess.game.service.main

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PositionData
import org.bson.BsonDocument
import org.rogach.scallop.*

/** Main of the application. */
@main def main(args: String*): Unit =
  val arguments: Args = Args(args)
  println("Hello world!")

/**
 * The parsed command line arguments accepted by this application.
 *
 * @param arguments the sequence of command line arguments to parse.
 *
 * @see [[https://github.com/scallop/scallop Scallop Documentation on Github]].
 */
class Args(private val arguments: Seq[String]) extends ScallopConf(arguments):
  verify()
