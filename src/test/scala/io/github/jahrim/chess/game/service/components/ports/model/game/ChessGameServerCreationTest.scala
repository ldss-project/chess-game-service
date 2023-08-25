package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}

/** A test for a [[ChessGameServer]] that has just been created. */
class ChessGameServerCreationTest
    extends AbstractChessGameServerTest(vertx => ChessGameServer(vertx)):
  describe("A chess game") {
    describe("when created") {
      it("should not be configured") {
        chessGameServer.getState.await.get.serverSituation shouldBe NotConfigured
      }
    }
  }
