package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameConfiguredException,
  GameNotReadyException,
  GameNotRunningException,
  GameNotWaitingForPromotionException,
  PlayerAlreadyExistingException
}
import io.github.jahrim.chess.game.service.components.ports.GameRecording.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that is waiting for players. */
class ChessGameServerWaitingForPlayersTest
    extends AbstractChessGameServerTest(
      AbstractChessGameServerTest.ChessGameWaitingForPlayersSupplier
    ):
  describe("A chess game") {
    describe("when waiting for players") {
      it("should allow players to retrieve its current state") {
        chessGameServer.getState.await.get.serverSituation shouldBe WaitingForPlayers
      }
      it("should allow players to join the game") {
        interactionTest()(
          before => {
            before.serverSituation shouldBe
              WaitingForPlayers
            before.gameState.gameConfiguration.playerOption(LegacyTeam.WHITE) shouldBe
              None
            before.gameState.gameConfiguration.playerOption(LegacyTeam.BLACK) shouldBe
              None
          },
          chessGameServer.join(LegacyWhitePlayer.default).await.get,
          after => {
            after.serverSituation shouldBe
              WaitingForPlayers
            after.gameState.gameConfiguration.playerOption(LegacyTeam.WHITE) shouldBe
              Some(LegacyWhitePlayer.default)
            after.gameState.gameConfiguration.playerOption(LegacyTeam.BLACK) shouldBe
              None
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
      it("should be ready after enough players join the game") {
        interactionTest()(
          before => {
            before.serverSituation shouldBe
              WaitingForPlayers
            before.gameState.gameConfiguration.playerOption(LegacyTeam.WHITE) shouldBe
              None
            before.gameState.gameConfiguration.playerOption(LegacyTeam.BLACK) shouldBe
              None
          }, {
            chessGameServer.join(LegacyWhitePlayer.default).await.get
            chessGameServer.join(LegacyBlackPlayer.default).await.get
          },
          after => {
            after.serverSituation shouldBe
              Ready
            after.gameState.gameConfiguration.playerOption(LegacyTeam.WHITE) shouldBe
              Some(LegacyWhitePlayer.default)
            after.gameState.gameConfiguration.playerOption(LegacyTeam.BLACK) shouldBe
              Some(LegacyBlackPlayer.default)
          }
        )
      }
      it(
        "should forbid players from joining the game when a player of their team is already present"
      ) {
        chessGameServer.join(LegacyWhitePlayer.default).await.get
        assertThrows[PlayerAlreadyExistingException](
          chessGameServer.join(LegacyWhitePlayer.default).await.get
        )
      }
      it("should forbid players from configuring the game") {
        assertThrows[GameConfiguredException](
          chessGameServer.configure(GameConfiguration.default).await.get
        )
      }
      it("should forbid players from starting the game") {
        assertThrows[GameNotReadyException](chessGameServer.start().await.get)
      }
      it("should forbid players from finding moves for their pieces") {
        assertThrows[GameNotRunningException](chessGameServer.findMoves("B2").await.get)
      }
      it("should forbid players from applying moves for their pieces") {
        assertThrows[GameNotRunningException](chessGameServer.applyMove("B2B3").await.get)
      }
      it("should forbid players from promoting their pieces") {
        assertThrows[GameNotWaitingForPromotionException](
          chessGameServer.promote(PromotionChoice.Queen).await.get
        )
      }
    }
  }
