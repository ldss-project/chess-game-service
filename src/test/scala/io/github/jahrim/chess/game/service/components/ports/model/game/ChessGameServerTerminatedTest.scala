package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.configuration.WhitePlayer as LegacyWhitePlayer
import io.github.jahrim.chess.game.service.components.events.ChessGameServiceEvent
import io.github.jahrim.chess.game.service.components.exceptions.*
import io.github.jahrim.chess.game.service.components.ports.GameRecording.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice,
  ServerState
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that has been terminated. */
class ChessGameServerTerminatedTest
    extends AbstractChessGameServerTest(AbstractChessGameServerTest.ChessGameTerminatedSupplier):
  describe("A chess game") {
    describe("when terminated") {
      it("should allow players to retrieve its current state") {
        val state: ServerState = chessGameServer.getState.await.get
        state.serverSituation shouldBe Terminated
        state.subscriptions.keys shouldBe empty
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
      it("should forbid players from stopping the game") {
        assertThrows[GameTerminatedException](chessGameServer.stop().await.get)
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
      it("should forbid players from subscribing to the game events") {
        assertThrows[GameTerminatedException](
          chessGameServer.subscribe[ChessGameServiceEvent](identity).await.get
        )
      }
      it("should forbid players from unsubscribing from the game events") {
        assertThrows[GameTerminatedException](
          chessGameServer.unsubscribe("SubscriptionId").await.get
        )
      }
    }
  }
