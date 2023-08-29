package io.github.jahrim.chess.game.service.components.adapters.http

import io.github.jahrim.chess.game.service.components.adapters.http.ChessGameHttpAdapter.{*, given}
import io.github.jahrim.chess.game.service.components.adapters.http.handlers.{
  LogHandler,
  PlayerConnectionHandler
}
import io.github.jahrim.chess.game.service.components.data.codecs.Codecs.{*, given}
import io.github.jahrim.chess.game.service.components.exceptions.*
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameConfiguration
import io.github.jahrim.hexarc.architecture.vertx.core.components.{Adapter, AdapterContext}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.{HttpMethod, HttpServerOptions}
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.{BodyHandler, CorsHandler}
import io.vertx.ext.web.{Router, RoutingContext}
import org.bson.{BsonDocument, BsonValue}

import scala.jdk.CollectionConverters.{SeqHasAsJava, SetHasAsJava}
import scala.util.Try

/**
 * An [[Adapter]] that enables http communication with a [[ChessGamePort]] of a service.
 *
 * @param httpOptions the specified http options.
 * @param allowedOrigins a sequence of sites that are allowed to use the api of this service.
 */
class ChessGameHttpAdapter(
    httpOptions: HttpServerOptions = HttpServerOptions().setHost("localhost").setPort(8080),
    allowedOrigins: Seq[String] = Seq()
) extends Adapter[ChessGamePort]:

  override protected def init(context: AdapterContext[ChessGamePort]): Unit =
    val router = Router.router(context.vertx)

    val cors: CorsHandler =
      CorsHandler
        .create()
        .addOrigins(allowedOrigins.asJava)
        .allowCredentials(true)
        .allowedMethods(
          Set(
            HttpMethod.HEAD,
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT
          ).asJava
        )

    router
      .route()
      .handler(cors)
      .handler(BodyHandler.create())
      .handler(LogHandler(context.log.info))
      .failureHandler(context => context.sendException(context.failure))

    router
      .get("/")
      .handler { message => message.response().send("Welcome to the Chess Game Service!") }

    router
      .post("/game")
      .handler(message =>
        val gameConfiguration = message.requireBodyParam("gameConfiguration").as[GameConfiguration]
        context.api
          .createGame(gameConfiguration)
          .map(gameId =>
            bson { "connection" :# { "websocket" :: s"/game/connect/$gameId/websocket" } }
          )
          .onSuccess(bson => message.sendBson(HttpResponseStatus.OK, bson))
          .onFailure(message.sendException)
      )

    router
      .get("/game")
      .handler(message =>
        context.api
          .findPublicGame()
          .map(gameId =>
            bson { "connection" :# { "websocket" :: s"/game/connect/$gameId/websocket" } }
          )
          .onSuccess(bson => message.sendBson(HttpResponseStatus.OK, bson))
          .onFailure(message.sendException)
      )

    router
      .get("/game/:gameId")
      .handler(message =>
        val gameId = message.requirePathParam("gameId")
        context.api
          .findPrivateGame(gameId)
          .map(gameId =>
            bson { "connection" :# { "websocket" :: s"/game/connect/$gameId/websocket" } }
          )
          .onSuccess(bson => message.sendBson(HttpResponseStatus.OK, bson))
          .onFailure(message.sendException)
      )

    router
      .route("/game/connect/:gameId/*")
      .subRouter(
        SockJSHandler
          .create(context.vertx)
          .socketHandler(PlayerConnectionHandler(context))
      )

    context.vertx
      .createHttpServer(httpOptions)
      .requestHandler(router)
      .listen(_ =>
        context.log.info(s"The server is up at ${httpOptions.getHost}:${httpOptions.getPort}")
      )
  end init

/** Companion object of [[ChessGameHttpAdapter]]. */
object ChessGameHttpAdapter:
  given Conversion[HttpResponseStatus, Int] = _.code()

  extension (self: RoutingContext) {

    /** Send a '200 OK' http response. */
    def sendOk(): Unit =
      self.response.setStatusCode(HttpResponseStatus.OK).send()

    /**
     * Send an http response with the specified status code and the specified
     * [[BsonDocument]] as json content.
     *
     * @param statusCode the specified status code.
     * @param bson       the specified [[BsonDocument]].
     */
    def sendBson(statusCode: Int, bson: BsonDocument): Unit =
      self.response
        .setStatusCode(statusCode)
        .putHeader("Content-Type", "application/json")
        .send(bson.as[JsonObject].encode())

    /**
     * Send the specified [[Throwable]] as an http response
     * [[BsonDocument]] as json content.
     *
     * @param throwable the specified [[Throwable]].
     */
    def sendException(throwable: Throwable): Unit =
      throwable.printStackTrace()
      self.sendBson(
        statusCode = throwable match {
          case _: MalformedInputException     => HttpResponseStatus.BAD_REQUEST
          case _: GameIdAlreadyTakenException => HttpResponseStatus.FORBIDDEN
          case _: GameAlreadyStartedException => HttpResponseStatus.FORBIDDEN
          case _: GameNotFoundException       => HttpResponseStatus.NOT_FOUND
          case _: NoAvailableGamesException   => HttpResponseStatus.NOT_FOUND
          case _: Throwable                   => HttpResponseStatus.INTERNAL_SERVER_ERROR
        },
        bson = bson {
          "type" :: throwable.getClass.getSimpleName
          "message" :: throwable.getMessage
        }
      )

    /**
     * Get the value of specified path parameter if present.
     *
     * @param paramName the name of the specified path parameter.
     * @return the value of the specified path parameter.
     * @throws MalformedInputException if no value is bound to the specified path parameter.
     */
    def requirePathParam(paramName: String): String =
      Option(self.pathParam(paramName)).getOrElse {
        throw MalformedInputException(s"Request missing path parameter '$paramName'.")
      }

    /**
     * Get the value of specified body parameter if present.
     *
     * @param path the path to the specified body parameter.
     * @return the value of the specified body parameter.
     * @throws MalformedInputException if no value is bound to the specified body parameter.
     */
    def requireBodyParam(path: String): BsonValue =
      Try {
        self.body.asJsonObject.asBson.asDocument.require(path)
      }.getOrElse {
        throw MalformedInputException(s"Request missing body parameter '$path'.")
      }
  }
