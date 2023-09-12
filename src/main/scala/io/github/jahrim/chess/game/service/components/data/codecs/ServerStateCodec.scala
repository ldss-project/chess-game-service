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

import io.github.jahrim.chess.game.service.components.data.codecs.ChessGameServiceExceptionCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameStateCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerSituationCodec.given
import io.github.jahrim.chess.game.service.components.exceptions.ChessGameServiceException
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameState,
  ServerSituation,
  ServerState,
  TimeConstraint
}
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ServerState ServerState]]. */
object ServerStateCodec:
  /** A given [[BsonDecoder]] for [[ServerState ServerState]]. */
  given serverStateDecoder: BsonDocumentDecoder[ServerState] = bson =>
    ServerState(
      serverSituation = bson.require("situation").as[ServerSituation],
      serverErrorOption = bson.require("error").as[Option[ChessGameServiceException]],
      // subscriptions consumers are not deserialized
      gameState = bson.require("gameState").as[GameState]
    )

  /** A given [[BsonEncoder]] for [[ServerState ServerState]]. */
  given serverStateEncoder: BsonDocumentEncoder[ServerState] = serverState =>
    bson {
      "situation" :: serverState.serverSituation
      "subscriptions" :: serverState.subscriptions.keys.toSeq
      "error" :: serverState.serverErrorOption
      "gameState" :: serverState.gameState
    }
