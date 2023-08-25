package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.board.ChessBoard.DSL.*
import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.{GameOverCause as LegacyGameOverCause, Team as LegacyTeam}
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.chess.engine.model.pieces.Pawn as LegacyPawn
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameConfiguredException,
  GameNotReadyException,
  GameNotWaitingForPlayersException,
  GameNotWaitingForPromotionException
}
import io.github.jahrim.chess.game.service.components.ports.GameRecording
import io.github.jahrim.chess.game.service.components.ports.GameRecording.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that is running. */
class ChessGameServerRunningTest
    extends AbstractChessGameServerTest(AbstractChessGameServerTest.ChessGameRunningSupplier):
  describe("A chess game") {
    describe("when running") {
      it("should allow players to retrieve its current state") {
        chessGameServer.getState.await.get.serverSituation shouldBe Running
      }
      it("should allow players to find moves for their pieces") {
        chessGameServer
          .findMoves("A2")
          .await
          .get
          .map(move => LegacyMove(move.from, move.to))
          .shouldEqual(Set("A2A3", "A2A4").map(stringToMove))
      }
      it("should allow players to apply moves for their pieces") {
        interactionTest()(
          before => {
            before.gameState.chessboard shouldEqual
              LegacyChessboard.standard
            before.gameState.moveHistory shouldBe
              MoveHistory.empty
            before.gameState.currentTurn shouldBe
              LegacyTeam.WHITE
          },
          chessGameServer.applyMove("A2A3").await.get,
          after => {
            after.gameState.chessboard shouldEqual
              LegacyChessboard {
                r | n | b | q | k | b | n | r
                p | p | p | p | p | p | p | p
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                P | * | * | * | * | * | * | *
                * | P | P | P | P | P | P | P
                R | N | B | Q | K | B | N | R
              }
            after.gameState.moveHistory shouldBe
              MoveHistory(Seq(LegacyPawn(LegacyTeam.WHITE) -> "A2A3"))
            after.gameState.currentTurn shouldBe
              LegacyTeam.BLACK
          }
        )
      }
      it("should allow players to stop the game") {
        stopGameTest()
      }
      it("should allow players to subscribe to the game events") {
        subscribeTest()
      }
      it("should allow players to unsubscribe from the game events") {
        unsubscribeTest()
      }
      it("should be waiting for a promotion after a pawn reaches the opponent's end") {
        GameRecording("A2A4", "B7B5", "A4B5", "B8C6", "B5B6", "C6B8", "B6B7", "B8C6")
          .apply(chessGameServer)
        interactionTest()(
          before => {
            before.serverSituation shouldBe
              Running
            before.gameState.chessboard shouldBe
              LegacyChessboard {
                r | * | b | q | k | b | n | r
                p | P | p | p | p | p | p | p
                * | * | n | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | P | P | P | P | P | P | P
                R | N | B | Q | K | B | N | R
              }
            before.gameState.gameSituation shouldBe
              GameSituation.None
            before.gameState.currentTurn shouldBe
              LegacyTeam.WHITE
          },
          chessGameServer.applyMove("B7B8").await.get,
          after => {
            after.serverSituation shouldBe
              Running
            after.gameState.chessboard shouldBe
              LegacyChessboard {
                r | P | b | q | k | b | n | r
                p | * | p | p | p | p | p | p
                * | * | n | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | P | P | P | P | P | P | P
                R | N | B | Q | K | B | N | R
              }
            after.gameState.gameSituation should
              matchPattern {
                case GameSituation.Promotion(promotingPawnPosition)
                    if promotingPawnPosition == stringToPosition("B8") =>
              }
            after.gameState.currentTurn shouldBe
              LegacyTeam.WHITE
          }
        )
      }
      it("should be over after a checkmate") {
        GameRecording("F2F3", "E7E6", "G2G4")(chessGameServer)
        interactionTest()(
          before => {
            before.serverSituation shouldBe
              Running
            before.gameState.chessboard shouldBe
              LegacyChessboard {
                r | n | b | q | k | b | n | r
                p | p | p | p | * | p | p | p
                * | * | * | * | p | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | P | *
                * | * | * | * | * | P | * | *
                P | P | P | P | P | * | * | P
                R | N | B | Q | K | B | N | R
              }
            before.gameState.gameSituation shouldBe
              GameSituation.None
            before.gameState.currentTurn shouldBe
              LegacyTeam.BLACK
            before.gameState.gameOverOption shouldBe
              None
          },
          chessGameServer.applyMove("D8H4").await.get,
          after => {
            after.serverSituation shouldBe
              Terminated
            after.gameState.chessboard shouldBe
              LegacyChessboard {
                r | n | b | * | k | b | n | r
                p | p | p | p | * | p | p | p
                * | * | * | * | p | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | P | q
                * | * | * | * | * | P | * | *
                P | P | P | P | P | * | * | P
                R | N | B | Q | K | B | N | R
              }
            after.gameState.gameSituation shouldBe
              GameSituation.Checkmate
            after.gameState.currentTurn shouldBe
              LegacyTeam.WHITE
            after.gameState.gameOverOption shouldBe
              Some(
                GameOver(
                  cause = LegacyGameOverCause.Checkmate,
                  winner = Some(LegacyBlackPlayer.default)
                )
              )
          }
        )
      }
      it("should forbid players from configuring the game") {
        assertThrows[GameConfiguredException](
          chessGameServer.configure(GameConfiguration.default).await.get
        )
      }
      it("should forbid players from joining the game") {
        assertThrows[GameNotWaitingForPlayersException](
          chessGameServer.join(LegacyWhitePlayer.default).await.get
        )
      }
      it("should forbid players from starting the game") {
        assertThrows[GameNotReadyException](chessGameServer.start().await.get)
      }
      it("should forbid players from promoting their pieces when not waiting for a promotion") {
        chessGameServer.getState.await.get.gameState.gameSituation shouldNot matchPattern {
          case GameSituation.Promotion(promotingPawnPosition) =>
        }
        assertThrows[GameNotWaitingForPromotionException](
          chessGameServer.promote(PromotionChoice.Queen).await.get
        )
      }
    }
  }
