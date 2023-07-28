package io.github.jahrim.chess.game.service.components.adapters.http

import io.github.chess.engine.model.configuration.{GameConfiguration, GameMode, TimeConstraint, WhitePlayer}
import io.github.jahrim.chess.game.service.components.adapters.http.handlers.LogHandler
import io.vertx.core.http.HttpServerOptions
import io.github.jahrim.hexarc.architecture.vertx.core.components.{Adapter, AdapterContext}
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort
import io.github.jahrim.chess.game.service.main.TimeConstraint
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.{Router, RoutingContext}
import io.github.jahrim.chess.game.service.util.extension.VertxFutureExtension.*
import io.github.jahrim.chess.game.service.util.extension.JsonObjectExtension.*
import io.github.jahrim.chess.game.service.util.extension.RoutingContextExtension.*
import io.github.jahrim.chess.game.service.components.data.GameConfigurationData
import io.github.jahrim.chess.game.service.components.data.codecs.TimeConstraintCodec

class ChessGameHttpAdapter(
                          options: HttpServerOptions = new HttpServerOptions:
                            setHost("localhost")
                            setPort(8080)
                          ) extends Adapter[ChessGamePort]:


  override protected def init(context: AdapterContext[ChessGamePort]): Unit =
    val router = Router.router(context.vertx)

    router
      .route()
      .handler(BodyHandler.create())
      .handler(LogHandler(context.log.info))

    router
      .get("/")
      .handler { message => message.response().send("Ciao, questo e' il chess game service") }

    router
      .post("/game")
      .handler { message =>
        future { message.requireBodyParam("gameConfiguration").as[GameConfigurationData] }
          .compose { context.api.createGame(_) }
          .onSuccess { ok(message) }
          .onFailure { fail(message) }
      }

    router
      .get("/game")
      .handler { message =>
        context.api.findPublicGame()
          .map { gameId =>
            bsonToJson(bson {
              "connection" :# {
                "websocket" :: s"game/$gameId"
              }
            }).encode()
          }
          .onSuccess { json => message.sendJson(json) }
          .onFailure { fail(message) }
      }

    router
      .get("game/:gameId")
      .handler { message =>
        future { message.requirePathParam("gameId") }
          .compose { context.api.joinPrivateGame(_) }
          .map { gameId =>
            bsonToJson(bson {
              "connection" :# {
                "websocket" :: s"game/$gameId"
              }
            }).encode()
          }
          .onSuccess { json => message.sendJson(json) }
          .onFailure { fail(message) }
      }

    context.vertx
      .createHttpServer(options)
      .requestHandler(router)
      .listen(_ => context.log.info("The server is up"))

  end init

  private def ok[T](message: RoutingContext): Handler[T] = _ => message.ok.send()

  private def fail(message: RoutingContext): Handler[Throwable] = e =>
    e.printStackTrace()
    val response: HttpServerResponse = e match
      case e: MalformedInputException => message.error(400, e)
      case e: Exception => message.error(500, e)
    response.send()


