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
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.sockjs.SockJSSocket
import org.bson.BsonDocument

/**
 * A [[WebsocketHandler]] that handles the connection of a player
 * with a chess game in this service.
 */
class PlayerConnectionHandler(gameManager: ChessGamePort) extends WebsocketHandler {
  override def handle(websocket: SockJSSocket): Unit =
    val gameId: Id = websocket.routingContext.requirePathParam("gameId")

    /** Setup the websocket communication from the client to the server. */
    def setupClientServerCommunication(): Unit =
      websocket
        .handler(data =>
          val message = data.toJsonObject.asBson.asDocument
          message("methodCall")
            .map(_.asDocument)
            .foreach(methodCall =>
              val input = methodCall("input")
              methodCall.require("method").as[String] match
                case "GetState" =>
                  gameManager
                    .getState(gameId)
                    .onSuccess(serverState =>
                      websocket.write(
                        bson {
                          "methodCall" :# {
                            "method" :: "GetState"
                            "output" :: serverState
                          }
                        }.as[JsonObject].encode()
                      )
                    )
                case "JoinGame" =>
                  gameManager.joinGame(gameId, input.require("player").as[LegacyPlayer])
                case "FindMoves" =>
                  gameManager
                    .findMoves(gameId, input.require("position").as[LegacyPosition])
                    .onSuccess(moves =>
                      websocket.write(
                        bson {
                          "methodCall" :# {
                            "method" :: "FindMoves"
                            "output" :: moves.toSeq
                          }
                        }.as[JsonObject].encode()
                      )
                    )
                case "ApplyMove" =>
                  gameManager.applyMove(gameId, input.require("move").as[LegacyMove])
                case "Promote" =>
                  gameManager.promote(
                    gameId,
                    input.require("promotionChoice").as[PromotionChoice]
                  )
            )
        )
        .closeHandler(_ => gameManager.deleteGame(gameId))

    /** Setup the websocket communication from the server to the client. */
    def setupServerClientCommunication(): Unit =
      gameManager.subscribe[ServerStateUpdateEvent](
        gameId,
        event => websocket.write(bson { "event" :: event }.as[JsonObject].encode())
      )

    setupClientServerCommunication()
    setupServerClientCommunication()
}
