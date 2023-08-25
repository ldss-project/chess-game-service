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
