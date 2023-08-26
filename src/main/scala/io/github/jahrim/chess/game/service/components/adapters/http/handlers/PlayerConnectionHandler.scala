package io.github.jahrim.chess.game.service.components.adapters.http.handlers

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.jahrim.chess.game.service.components.adapters.http.ChessGameHttpAdapter.*
import io.github.jahrim.chess.game.service.components.data.codecs.Codecs.given
import io.github.jahrim.chess.game.service.components.events.{
  ServerStateUpdateEvent,
  TurnUpdateEvent
}
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort.Id
import io.github.jahrim.chess.game.service.components.ports.model.game.state.PromotionChoice
import io.github.jahrim.hexarc.architecture.vertx.core.components.AdapterContext
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.sockjs.SockJSSocket
import org.bson.BsonDocument

import java.nio.charset.StandardCharsets

/**
 * A [[WebsocketHandler]] that handles the connection of a player
 * with a chess game in this service.
 */
class PlayerConnectionHandler(context: AdapterContext[ChessGamePort]) extends WebsocketHandler {
  extension (self: SockJSSocket) {

    /**
     * @param bson the specified [[BsonDocument]].
     * @return send the specified [[BsonDocument]] through this websocket.
     */
    private def writeBson(bson: BsonDocument): Future[Void] =
      context.log.info(s"Sending websocket message: ${bson.toString}")
      self.write(bson.as[JsonObject].encode())

    /**
     * @param handler the specified handler.
     * @return register the specified handler to execute whenever a [[BsonDocument]]
     *         is received through this websocket.
     */
    private def receiveBson(handler: BsonDocument => Unit): SockJSSocket =
      self.handler(data =>
        context.log.info(s"Receiving websocket message: ${data.toString}")
        handler(data.toJsonObject.asBson.asDocument)
      )
  }

  override def handle(websocket: SockJSSocket): Unit =
    val gameId: Id = websocket.routingContext.requirePathParam("gameId")

    /** Setup the websocket communication from the client to the server. */
    def setupClientServerCommunication(): Unit =
      websocket
        .receiveBson(message =>
          message("methodCall")
            .map(_.asDocument)
            .foreach(methodCall =>
              val input = methodCall("input")
              methodCall.require("method").as[String] match
                case "GetState" =>
                  context.api
                    .getState(gameId)
                    .onSuccess(serverState =>
                      websocket.writeBson(
                        bson {
                          "methodCall" :# {
                            "method" :: "GetState"
                            "output" :: serverState
                          }
                        }
                      )
                    )
                case "JoinGame" =>
                  context.api.joinGame(gameId, input.require("player").as[LegacyPlayer])
                case "FindMoves" =>
                  context.api
                    .findMoves(gameId, input.require("position").as[LegacyPosition])
                    .onSuccess(moves =>
                      websocket.writeBson(
                        bson {
                          "methodCall" :# {
                            "method" :: "FindMoves"
                            "output" :: moves.toSeq
                          }
                        }
                      )
                    )
                case "ApplyMove" =>
                  context.api.applyMove(gameId, input.require("move").as[LegacyMove])
                case "Promote" =>
                  context.api.promote(
                    gameId,
                    input.require("promotionChoice").as[PromotionChoice]
                  )
            )
        )
        .closeHandler(_ => context.api.deleteGame(gameId))

    /** Setup the websocket communication from the server to the client. */
    def setupServerClientCommunication(): Unit =
      context.api.subscribe[ServerStateUpdateEvent](
        gameId,
        event => websocket.writeBson(bson { "event" :: event })
      )

    setupClientServerCommunication()
    setupServerClientCommunication()
}
