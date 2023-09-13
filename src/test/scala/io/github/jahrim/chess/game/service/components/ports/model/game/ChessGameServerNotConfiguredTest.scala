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
package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameNotReadyException,
  GameNotRunningException,
  GameNotWaitingForPlayersException
}
import io.github.jahrim.chess.game.service.components.ports.GameRecording.given
import io.github.jahrim.chess.game.service.components.ports.model.game.AbstractChessGameServerTest
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that is not yet configured. */
class ChessGameServerNotConfiguredTest
    extends AbstractChessGameServerTest(AbstractChessGameServerTest.ChessGameNotConfiguredSupplier):
  describe("A chess game") {
    describe("when not configured") {
      it("should allow players to retrieve its current state") {
        chessGameServer.getState.await.get.serverSituation shouldBe NotConfigured
      }
      it("should allow players to configure the game") {
        val gameConfiguration: GameConfiguration = GameConfiguration(gameId = "ChessGameTest")
        interactionTest()(
          before => {
            before.gameState.gameConfiguration.gameIdOption shouldBe
              None
            before.serverSituation shouldBe
              NotConfigured
          },
          chessGameServer.configure(gameConfiguration).await,
          after => {
            after.gameState.gameConfiguration.gameIdOption shouldBe
              gameConfiguration.gameIdOption
            after.serverSituation shouldBe
              WaitingForPlayers
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
      it("should be ready after being configured with two players") {
        val readyGameConfiguration: GameConfiguration = GameConfiguration(
          gameId = "ChessGameTest",
          whitePlayer = LegacyWhitePlayer.default,
          blackPlayer = LegacyBlackPlayer.default
        )
        interactionTest()(
          before => {
            before.gameState.gameConfiguration.gameIdOption shouldBe
              None
            before.gameState.gameConfiguration.playerOption(LegacyTeam.WHITE) shouldBe
              None
            before.gameState.gameConfiguration.playerOption(LegacyTeam.BLACK) shouldBe
              None
            before.serverSituation shouldBe
              NotConfigured
          },
          chessGameServer.configure(readyGameConfiguration).await,
          after => {
            after.gameState.gameConfiguration.gameIdOption shouldBe
              readyGameConfiguration.gameIdOption
            after.gameState.gameConfiguration.playerOption(LegacyTeam.WHITE) shouldBe
              readyGameConfiguration.whitePlayerOption
            after.gameState.gameConfiguration.playerOption(LegacyTeam.BLACK) shouldBe
              readyGameConfiguration.blackPlayerOption
            after.serverSituation shouldBe
              Ready
          }
        )
      }
      it("should forbid players from starting the game") {
        assertThrows[GameNotReadyException](chessGameServer.start().await.get)
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
        assertThrows[GameNotRunningException](
          chessGameServer.promote(PromotionChoice.Queen).await.get
        )
      }
    }
  }
