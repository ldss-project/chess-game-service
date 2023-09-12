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
package io.github.jahrim.chess.game.service.main

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.adapters.http.ChessGameHttpAdapter
import io.github.jahrim.chess.game.service.components.ports.{ChessGameModel, ChessGamePort}
import io.github.jahrim.chess.game.service.components.proxies.statistics.StatisticsServiceProxy
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import org.bson.BsonDocument
import org.rogach.scallop.*

/** Main of the application. */
@main def main(args: String*): Unit =
  val arguments: Args = Args(args)

  val vertx: Vertx = Vertx.vertx()

  DeploymentGroup.deploySingle(vertx) {
    new Service:
      name = "ChessGameService"

      new Port[ChessGamePort]:
        model = ChessGameModel(
          statisticsService = StatisticsServiceProxy(
            serviceHost = arguments.statisticsService(),
            vertx = vertx
          )
        )

        new Adapter(
          adapter = ChessGameHttpAdapter(
            httpOptions = HttpServerOptions()
              .setHost(arguments.httpHost())
              .setPort(arguments.httpPort()),
            allowedOrigins = arguments.allowedOrigins()
          )
        )
  }

/**
 * The parsed command line arguments accepted by this application.
 *
 * @param arguments the sequence of command line arguments to parse.
 *
 * @see [[https://github.com/scallop/scallop Scallop Documentation on Github]].
 */
class Args(private val arguments: Seq[String]) extends ScallopConf(arguments):
  val statisticsService: ScallopOption[String] = opt[String](
    name = "statistics-service",
    descr = "The host of the statistics service where the result of the matches will be stored.",
    default = Some("localhost:8082"),
    required = false
  )
  val httpHost: ScallopOption[String] = opt[String](
    name = "http-host",
    descr = "The server host for the http adapter of this service.",
    default = Some("localhost"),
    required = false
  )
  val httpPort: ScallopOption[Int] = opt[Int](
    name = "http-port",
    descr = "The server port for the http adapter of this service.",
    default = Some(8080),
    required = false
  )
  val allowedOrigins: ScallopOption[Seq[String]] = opt[String](
    name = "allowed-origins",
    descr = "A list of colon separated origins that are allowed to access this service.",
    default = Some(""),
    required = false
  ).map(_.split(";").filter(_.nonEmpty).toSeq)
  verify()
