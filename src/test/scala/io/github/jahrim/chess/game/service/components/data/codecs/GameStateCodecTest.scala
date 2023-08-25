package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.GameStateCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameState
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[GameStateCodec]]. */
class GameStateCodecTest extends BsonCodecTest(GameStateCodecTest)

/** Companion object of [[GameStateCodecTest]]. */
object GameStateCodecTest extends BsonCodecTest.BsonCodecData[GameState]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[GameState](
      name = "GameState",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      },
      expected = state.GameState(
        chessboard = LegacyChessboardCodecTest.dataSample,
        moveHistory = MoveHistoryCodecTest.dataSample,
        currentTurn = LegacyTeamCodecTest.dataSample,
        gameConfiguration = GameConfigurationCodecTest.dataSample,
        gameSituation = GameSituationCodecTest.dataSample,
        gameOverOption = Some(GameOverCodecTest.dataSample),
        timers = ChessTimerMapCodecTest.dataSample
      )
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[GameState](
      name = "GameState with no chessboard",
      sample = bson {
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[GameState](
      name = "GameState with no history",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[GameState](
      name = "GameState with no current turn",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[GameState](
      name = "GameState with no configuration",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[GameState](
      name = "GameState with no game situation",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[GameState](
      name = "GameState with no game over",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "timers" :: ChessTimerMapCodecTest.bsonSample
      }
    ),
    WrongDecodeSample[GameState](
      name = "GameState with no timers",
      sample = bson {
        "chessboard" :: LegacyChessboardCodecTest.bsonSample
        "moveHistory" :: MoveHistoryCodecTest.bsonSample
        "currentTurn" :: LegacyTeamCodecTest.bsonSample
        "configuration" :: GameConfigurationCodecTest.bsonSample
        "situation" :: GameSituationCodecTest.bsonSample
        "gameOver" :: GameOverCodecTest.bsonSample
      }
    )
  )
