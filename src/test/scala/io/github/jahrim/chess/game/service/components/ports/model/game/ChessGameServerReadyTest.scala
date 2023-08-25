package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.configuration.WhitePlayer as LegacyWhitePlayer
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameConfiguredException,
  GameNotRunningException,
  GameNotWaitingForPlayersException,
  GameNotWaitingForPromotionException
}
import io.github.jahrim.chess.game.service.components.ports.GameRecording.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that is ready to start. */
class ChessGameServerReadyTest
    extends AbstractChessGameServerTest(AbstractChessGameServerTest.ChessGameReadySupplier):
  describe("A chess game") {
    describe("when ready to start") {
      it("should allow players to retrieve its current state") {
        chessGameServer.getState.await.get.serverSituation shouldBe Ready
      }
      it("should allow players to start the game") {
        interactionTest(_.serverSituation)(
          before => before shouldBe Ready,
          chessGameServer.start().await,
          after => after shouldBe Running
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
