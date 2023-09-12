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

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.ChessTimerMapCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameConfigurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameOverCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameSituationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyChessboardCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveHistoryCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameState.ChessTimerMap
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[GameState GameState]]. */
object GameStateCodec:
  /** A given [[BsonDecoder]] for [[GameState GameState]]. */
  given gameStateDecoder: BsonDocumentDecoder[GameState] = bson =>
    GameState(
      chessboard = bson.require("chessboard").as[LegacyChessboard],
      moveHistory = bson.require("moveHistory").as[MoveHistory],
      currentTurn = bson.require("currentTurn").as[LegacyTeam],
      gameConfiguration = bson.require("configuration").as[GameConfiguration],
      gameSituation = bson.require("situation").as[GameSituation],
      gameOverOption = bson.require("gameOver").as[Option[GameOver]],
      timers = bson.require("timers").as[ChessTimerMap]
    )

  /** A given [[BsonEncoder]] for [[GameState GameState]]. */
  given gameStateEncoder: BsonDocumentEncoder[GameState] = gameState =>
    bson {
      "chessboard" :: gameState.chessboard
      "moveHistory" :: gameState.moveHistory
      "currentTurn" :: gameState.currentTurn
      "configuration" :: gameState.gameConfiguration
      "situation" :: gameState.gameSituation
      "gameOver" :: gameState.gameOverOption
      "timers" :: gameState.timers
    }
