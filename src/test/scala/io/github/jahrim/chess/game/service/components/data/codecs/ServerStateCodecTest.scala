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
package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.ServerStateCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState.SubscriptionMap
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.Vertx

/** A [[BsonCodecTest]] for [[ServerStateCodec]]. */
class ServerStateCodecTest extends BsonCodecTest(ServerStateCodecTest)

/** Companion object of [[ServerStateCodecTest]]. */
object ServerStateCodecTest extends BsonCodecTest.BsonCodecData[ServerState]:
  private val vertx: Vertx = Vertx.vertx()
  private val subscriptionMap: SubscriptionMap = Map(
    "SubscriptionId#1" -> vertx.eventBus.consumer("Address1"),
    "SubscriptionId#2" -> vertx.eventBus.consumer("Address2")
  )

  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[ServerState](
      name = "ServerState",
      sample = bson {
        "situation" :: ServerSituationCodecTest.bsonSample
        "subscriptions" :: subscriptionMap.keys.toSeq
        "error" :: ChessGameServiceExceptionCodecTest.bsonSample
        "gameState" :: GameStateCodecTest.bsonSample
      },
      expected = ServerState(
        serverSituation = ServerSituationCodecTest.dataSample,
        serverErrorOption = Some(ChessGameServiceExceptionCodecTest.dataSample),
        subscriptions = Map(), // message consumers are not deserialized
        gameState = GameStateCodecTest.dataSample
      )
    ),
    DecodeSample[ServerState](
      name = "ServerState with no subscriptions",
      sample = bson {
        "situation" :: ServerSituationCodecTest.bsonSample
        "error" :: ChessGameServiceExceptionCodecTest.bsonSample
        "gameState" :: GameStateCodecTest.bsonSample
      },
      expected = ServerState(
        serverSituation = ServerSituationCodecTest.dataSample,
        serverErrorOption = Some(ChessGameServiceExceptionCodecTest.dataSample),
        gameState = GameStateCodecTest.dataSample
      )
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[ServerState](
      name = "ServerState with no situation",
      sample = bson {
        "subscriptions" :: Seq("SubscriptionId#1", "SubscriptionId#2")
        "error" :: ChessGameServiceExceptionCodecTest.bsonSample
        "gameState" :: GameStateCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[ServerState](
      name = "ServerState with no error",
      sample = bson {
        "situation" :: ServerSituationCodecTest.bsonSample
        "subscriptions" :: Seq("SubscriptionId#1", "SubscriptionId#2")
        "gameState" :: GameStateCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[ServerState](
      name = "ServerState with no game state",
      sample = bson {
        "situation" :: ServerSituationCodecTest.bsonSample
        "subscriptions" :: Seq("SubscriptionId#1", "SubscriptionId#2")
        "error" :: ChessGameServiceExceptionCodecTest.bsonSample
      }
    )
  )

  override def encodeSamples: EncodeSampleIterable = Seq(
    EncodeSample[ServerState](
      name = "ServerState",
      sample = ServerState(
        serverSituation = ServerSituationCodecTest.dataSample,
        serverErrorOption = Some(ChessGameServiceExceptionCodecTest.dataSample),
        subscriptions = subscriptionMap,
        gameState = GameStateCodecTest.dataSample
      ),
      expected = bson {
        "situation" :: ServerSituationCodecTest.bsonSample
        "subscriptions" :: subscriptionMap.keys.toSeq
        "error" :: ChessGameServiceExceptionCodecTest.bsonSample
        "gameState" :: GameStateCodecTest.bsonSample
      }
    ),
    EncodeSample[ServerState](
      name = "ServerState with no subscriptions",
      sample = ServerState(
        serverSituation = ServerSituationCodecTest.dataSample,
        serverErrorOption = Some(ChessGameServiceExceptionCodecTest.dataSample),
        gameState = GameStateCodecTest.dataSample
      ),
      expected = bson {
        "situation" :: ServerSituationCodecTest.bsonSample
        "error" :: ChessGameServiceExceptionCodecTest.bsonSample
        "subscriptions" :: Seq()
        "gameState" :: GameStateCodecTest.bsonSample
      }
    )
  )
