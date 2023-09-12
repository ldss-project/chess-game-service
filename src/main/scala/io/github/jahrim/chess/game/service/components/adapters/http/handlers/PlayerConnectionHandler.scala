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
package io.github.jahrim.chess.game.service.components.adapters.http.handlers

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.jahrim.chess.game.service.components.adapters.http.ChessGameHttpAdapter.*
import io.github.jahrim.chess.game.service.components.data.codecs.Codecs.given
import io.github.jahrim.chess.game.service.components.events.*
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort.Id
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameSituation,
  PromotionChoice
}
import io.github.jahrim.hexarc.architecture.vertx.core.components.AdapterContext
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
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
class PlayerConnectionHandler(context: AdapterContext[ChessGamePort]) extends WebsocketHandler:
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
                            "output" :# { "serverState" :: serverState }
                          }
                        }
                      )
                    )
                case "JoinGame" =>
                  context.api
                    .joinGame(gameId, input.require("player").as[LegacyPlayer])
                    .onSuccess(_ =>
                      websocket.writeBson(
                        bson {
                          "methodCall" :# {
                            "method" :: "JoinGame"
                          }
                        }
                      )
                    )
                case "FindMoves" =>
                  context.api
                    .findMoves(gameId, input.require("position").as[LegacyPosition])
                    .onSuccess(moves =>
                      websocket.writeBson(
                        bson {
                          "methodCall" :# {
                            "method" :: "FindMoves"
                            "output" :# { "moves" :: moves.toSeq }
                          }
                        }
                      )
                    )
                case "ApplyMove" =>
                  context.api
                    .applyMove(gameId, input.require("move").as[LegacyMove])
                    .onSuccess(_ =>
                      websocket.writeBson(
                        bson {
                          "methodCall" :# {
                            "method" :: "ApplyMove"
                          }
                        }
                      )
                    )
                case "Promote" =>
                  context.api
                    .promote(gameId, input.require("promotionChoice").as[PromotionChoice])
                    .onSuccess(_ =>
                      websocket.writeBson(
                        bson {
                          "methodCall" :# {
                            "method" :: "Promote"
                          }
                        }
                      )
                    )
            )
        )
        .closeHandler(_ => context.api.deleteGame(gameId))

    /** Setup the websocket communication from the server to the client. */
    def setupServerClientCommunication(): Unit =
      context.api.subscribe[ServerStateUpdateEvent](
        gameId,
        event =>
          websocket.writeBson(
            bson {
              "event" :: (
                event match
                  case e: ChessboardUpdateEvent =>
                    encodeEventWithPayload[
                      ChessboardUpdateEvent,
                      ChessboardUpdateEvent#PayloadType
                    ](e)
                  case e: GameOverUpdateEvent =>
                    encodeEventWithPayload[GameOverUpdateEvent, GameOverUpdateEvent#PayloadType](e)
                  case e: GameSituationUpdateEvent =>
                    encodeEventWithPayload[
                      GameSituationUpdateEvent,
                      GameSituationUpdateEvent#PayloadType
                    ](e)
                  case e: MoveHistoryUpdateEvent =>
                    encodeEventWithPayload[
                      MoveHistoryUpdateEvent,
                      MoveHistoryUpdateEvent#PayloadType
                    ](e)
                  case e: WhitePlayerUpdateEvent =>
                    encodeEventWithPayload[
                      WhitePlayerUpdateEvent,
                      WhitePlayerUpdateEvent#PayloadType
                    ](e)
                  case e: BlackPlayerUpdateEvent =>
                    encodeEventWithPayload[
                      BlackPlayerUpdateEvent,
                      BlackPlayerUpdateEvent#PayloadType
                    ](e)
                  case e: WhiteTimerUpdateEvent =>
                    encodeEventWithPayload[
                      WhiteTimerUpdateEvent,
                      WhiteTimerUpdateEvent#PayloadType
                    ](e)
                  case e: BlackTimerUpdateEvent =>
                    encodeEventWithPayload[
                      BlackTimerUpdateEvent,
                      BlackTimerUpdateEvent#PayloadType
                    ](e)
                  case e: ServerErrorUpdateEvent =>
                    encodeEventWithPayload[
                      ServerErrorUpdateEvent,
                      ServerErrorUpdateEvent#PayloadType
                    ](e)
                  case e: ServerSituationUpdateEvent =>
                    encodeEventWithPayload[
                      ServerSituationUpdateEvent,
                      ServerSituationUpdateEvent#PayloadType
                    ](e)
                  case e: SubscriptionUpdateEvent =>
                    encodeEventWithPayload[
                      SubscriptionUpdateEvent,
                      SubscriptionUpdateEvent#PayloadType
                    ](e)
                  case e: TurnUpdateEvent =>
                    encodeEventWithPayload[
                      TurnUpdateEvent,
                      TurnUpdateEvent#PayloadType
                    ](e)
                  case _ => event.asBson.asDocument
              )
            }
          )
      )

    setupClientServerCommunication()
    setupServerClientCommunication()

  /**
   * Encode the specified [[Event]] with [[Event.Payload Payload]].
   *
   * @param event the specified [[Event]] with [[Event.Payload Payload]].
   * @param bsonDecoder the given [[BsonDecoder]] for the [[Event.Payload Payload]]
   *                    of the specified [[Event]].
   * @param bsonEncoder the given [[BsonEncoder]] for the [[Event.Payload Payload]]
   *                    of the specified [[Event]].
   * @tparam E the type of the specified [[Event]] with [[Event.Payload Payload]].
   * @tparam PayloadType the type of the [[Event.Payload Payload]] of the specified
   *                     [[Event]].
   * @return a [[BsonDocument]] representing this [[Event]] with [[Event.Payload Payload]].
   */
  private def encodeEventWithPayload[
      E <: Event with Event.Payload[PayloadType],
      PayloadType
  ](event: E)(using
      bsonDecoder: BsonDecoder[PayloadType],
      bsonEncoder: BsonEncoder[PayloadType]
  ): BsonDocument =
    event.asBson(using eventWithPayloadEncoder[E, PayloadType]).asDocument
