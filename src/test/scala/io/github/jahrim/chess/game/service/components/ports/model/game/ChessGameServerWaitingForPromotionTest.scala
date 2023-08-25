package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.board.ChessBoard.DSL.*
import io.github.chess.engine.model.configuration.WhitePlayer as LegacyWhitePlayer
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameConfiguredException,
  GameNotReadyException,
  GameNotWaitingForPlayersException,
  GameWaitingForPromotionException
}
import io.github.jahrim.chess.game.service.components.ports.GameRecording
import io.github.jahrim.chess.game.service.components.ports.GameRecording.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  GameSituation,
  PromotionChoice,
  ServerState
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that is waiting for a promotion. */
class ChessGameServerWaitingForPromotionTest
    extends AbstractChessGameServerTest(vertx =>
      val chessGame: ChessGameServer = AbstractChessGameServerTest.ChessGameRunningSupplier(vertx)
      GameRecording("A2A4", "B7B5", "A4B5", "B8C6", "B5B6", "C6B8", "B6B7", "B8C6", "B7B8")
        .apply(chessGame)
      chessGame
    ):
  describe("A chess game") {
    describe("when waiting for the promotion of a pawn") {
      it("should allow players to retrieve its current state") {
        val state: ServerState = chessGameServer.getState.await.get
        state.serverSituation shouldBe Running
        state.gameState.gameSituation should matchPattern {
          case GameSituation.Promotion(promotingPawnPosition) =>
        }
      }
      it("should allow players to promote their pieces") {
        interactionTest()(
          before => {
            before.serverSituation shouldEqual
              Running
            before.gameState.chessboard shouldEqual
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
            before.gameState.gameSituation should
              matchPattern {
                case GameSituation.Promotion(promotingPawnPosition)
                    if promotingPawnPosition == stringToPosition("B8") =>
              }
            before.gameState.currentTurn shouldBe
              LegacyTeam.WHITE
          },
          chessGameServer.promote(PromotionChoice.Queen).await.get,
          after => {
            after.serverSituation shouldEqual
              Running
            after.gameState.chessboard shouldEqual
              LegacyChessboard {
                r | Q | b | q | k | b | n | r
                p | * | p | p | p | p | p | p
                * | * | n | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | * | * | * | * | * | * | *
                * | P | P | P | P | P | P | P
                R | N | B | Q | K | B | N | R
              }
            after.gameState.gameSituation shouldEqual
              GameSituation.None
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
      it("should forbid players from finding moves for their pieces") {
        assertThrows[GameWaitingForPromotionException](
          chessGameServer.findMoves("B2").await.get
        )
      }
      it("should forbid players from applying moves for their pieces") {
        assertThrows[GameWaitingForPromotionException](
          chessGameServer.applyMove("B2B3").await.get
        )
      }
    }
  }
