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
package io.github.jahrim.chess.game.service.components.ports

import io.github.chess.engine.model.board.ChessBoardBuilder.DSL.*
import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.jahrim.chess.game.service.components.events.ChessGameServiceEvent
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameAlreadyStartedException,
  GameIdAlreadyTakenException,
  GameNotFoundException,
  NoAvailableGamesException
}
import io.github.jahrim.chess.game.service.components.ports.ChessGamePort.{ChessGameMap, Id}
import io.github.jahrim.chess.game.service.components.ports.GameRecording.{*, given}
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice,
  ServerState
}
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.ChessGameServer
import io.github.jahrim.chess.game.service.components.ports.{ChessGameModel, ChessGamePort}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.vertx.core.{Future, Promise, Vertx}
import test.AbstractTest

/** A test for [[ChessGameModel]]. */
class ChessGameModelTest extends AbstractTest:
  private val _vertx: Vertx = Vertx.vertx()
  private var _deployment: Deployment = _
  private var _chessGameModel: ChessGameModel = _
  private def chessGameModel: ChessGameModel = this._chessGameModel

  before {
    this._chessGameModel = ChessGameModel( /* TODO pass a statistics proxy */ )
    this._deployment = deployService(this._vertx, this._chessGameModel)
  }

  describe("The model of a ChessGamePort") {
    describe("when created") {
      it("should have no chess game registered") {
        chessGameModel.getGames.await.get shouldBe empty
      }
    }

    describe("when creating a new game") {
      it("should register the game created") {
        val createdGameId: Id = chessGameModel.createGame(GameConfiguration.default).await.get
        chessGameModel.getGames.await.get.keys should contain(createdGameId)
      }
      it("should allow the user to create a game with a custom configuration") {
        val customConfiguration: GameConfiguration = GameConfiguration(gameId = "CustomId")
        val createdGameId: Id = chessGameModel.createGame(customConfiguration).await.get
        val createdGameConfiguration: GameConfiguration =
          chessGameModel.getState(createdGameId).await.get.gameState.gameConfiguration

        createdGameId shouldEqual customConfiguration.gameIdOption.get
        chessGameModel.getGames.await.get.keys should contain(createdGameId)
        createdGameConfiguration shouldEqual customConfiguration
      }
      it("should forbid the user from creating a game whose id has been already taken") {
        val customConfiguration: GameConfiguration = GameConfiguration(gameId = "CustomId")
        chessGameModel.createGame(customConfiguration).await.get

        assertThrows[GameIdAlreadyTakenException](
          chessGameModel.createGame(customConfiguration).await.get
        )
      }
    }

    describe("when deleting a game") {
      it("should allow the user to delete a registered game") {
        val createdGameId: Id = createGame()
        chessGameModel.getGames.await.get.keys should contain(createdGameId)

        chessGameModel.deleteGame(createdGameId).await.get
        chessGameModel.getGames.await.get.keys shouldNot contain(createdGameId)
      }
      it("should stop the game before deleting it") {
        val createdGameId: Id = createGame()
        val createdGame: ChessGameServer = chessGameModel.getGames.await.get.apply(createdGameId)
        createdGame.getState.await.get.serverSituation shouldNot
          be(Terminated)
        chessGameModel.deleteGame(createdGameId).await.get
        createdGame.getState.await.get.serverSituation should
          be(Terminated)
      }
      it("should forbid the user from deleting a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.deleteGame("NonRegisteredGameId").await.get
        )
      }
    }

    describe("when finding a public game") {
      it("should let the user know the id of a public game") {
        val publicGameIds: Seq[Id] = Seq.fill(5)(createPublicGame())
        val privateGameIds: Seq[Id] = Seq.fill(5)(createPrivateGame())
        val gameFoundId: Id = chessGameModel.findPublicGame().await.get
        publicGameIds should contain(gameFoundId)
        privateGameIds shouldNot contain(gameFoundId)
      }
      it("should let the user know that there are no public games registered") {
        Seq.fill(5)(createPrivateGame())
        assertThrows[NoAvailableGamesException](chessGameModel.findPublicGame().await.get)
      }
    }

    describe("when finding a private game") {
      it("should let the user know the id of a requested private game") {
        val publicGameIds: Seq[Id] = Seq.fill(5)(createPublicGame())
        val privateGameIds: Seq[Id] = Seq.fill(5)(createPrivateGame())
        val requestedGameId: Id = privateGameIds.head
        val gameFoundId: Id = chessGameModel.findPrivateGame(requestedGameId).await.get
        privateGameIds should contain(gameFoundId)
        publicGameIds shouldNot contain(gameFoundId)
      }
      it(
        "should let the user know that there are no registered private games with the requested id"
      ) {
        Seq.fill(5)(createPrivateGame())
        assertThrows[GameNotFoundException](
          chessGameModel.findPrivateGame("NotRegisteredGameId").await.get
        )
      }
      it("should let the user know that the game with the requested id is already running") {
        val privateGameId: Id = makeRunningGame(createPrivateGame())
        val requestedGameId: Id = privateGameId
        assertThrows[GameAlreadyStartedException](
          chessGameModel.findPrivateGame(requestedGameId).await.get
        )
      }
    }

    describe("when retrieving the state of a game") {
      it("should let the user know the state of a registered game") {
        val gameId: Id = createGame()
        chessGameModel.getState(gameId).await.get shouldEqual
          chessGameModel.getGames.await.get.apply(gameId).getState.await.get
      }
      it("should forbid the user from knowing the state of a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.getState("NotRegisteredGameId").await.get
        )
      }
    }

    describe("when joining a game") {
      it("should let the user join a registered game") {
        val gameId: Id = createGame()
        interactionTest(gameId, _.gameState.gameConfiguration.whitePlayerOption)(
          before => before shouldEqual None,
          chessGameModel.joinGame(gameId, LegacyWhitePlayer.default).await.get,
          after => after shouldEqual Some(LegacyWhitePlayer.default)
        )
      }
      it("should forbid the user from knowing the state of a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.joinGame("NotRegisteredGameId", LegacyWhitePlayer.default).await.get
        )
      }
    }

    describe("when finding the moves of a piece in a game") {
      it("should let the user find the moves a piece in a registered game") {
        val gameId: Id = makeRunningGame(createGame())
        chessGameModel
          .findMoves(gameId, "A2")
          .await
          .get
          .map(move => LegacyMove(move.from, move.to))
          .shouldEqual(Set("A2A3", "A2A4").map(stringToMove))
      }
      it("should forbid the user from knowing the state of a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.findMoves("NotRegisteredGameId", "A2").await.get
        )
      }
    }

    describe("when applying a move in a game") {
      it("should let the user apply a moves in a registered game") {
        val gameId: Id = makeRunningGame(createGame())
        interactionTest(gameId, _.gameState.chessboard)(
          before =>
            before shouldEqual LegacyChessboard {
              r | n | b | q | k | b | n | r
              p | p | p | p | p | p | p | p
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              P | P | P | P | P | P | P | P
              R | N | B | Q | K | B | N | R
            },
          chessGameModel.applyMove(gameId, "A2A3").await.get,
          after =>
            after shouldEqual LegacyChessboard {
              r | n | b | q | k | b | n | r
              p | p | p | p | p | p | p | p
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              P | * | * | * | * | * | * | *
              * | P | P | P | P | P | P | P
              R | N | B | Q | K | B | N | R
            }
        )
      }

      it("should forbid the user from applying a move in a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.applyMove("NotRegisteredGameId", "A2A3").await.get
        )
      }
    }

    describe("when promoting a pawn in a game") {
      it("should let the user promote a pawn in a registered game") {
        val gameId: Id = makeRunningGame(createGame())
        GameRecording("A2A4", "B7B5", "A4B5", "B8C6", "B5B6", "C6B8", "B6B7", "B8C6", "B7B8")
          .apply(chessGameModel.getGames.await.get.apply(gameId))
        interactionTest(gameId, _.gameState.chessboard)(
          before =>
            before shouldEqual LegacyChessboard {
              r | P | b | q | k | b | n | r
              p | * | p | p | p | p | p | p
              * | * | n | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | P | P | P | P | P | P | P
              R | N | B | Q | K | B | N | R
            },
          chessGameModel.promote(gameId, PromotionChoice.Queen).await.get,
          after =>
            after shouldEqual LegacyChessboard {
              r | Q | b | q | k | b | n | r
              p | * | p | p | p | p | p | p
              * | * | n | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | * | * | * | * | * | * | *
              * | P | P | P | P | P | P | P
              R | N | B | Q | K | B | N | R
            }
        )
      }
      it("should forbid the user from promoting a pawn in a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.promote("NotRegisteredGameId", PromotionChoice.Queen).await.get
        )
      }
    }

    describe("when subscribing to an event type in a game") {
      it("should let the user subscribe to an event type in a registered game") {
        val gameId: Id = createGame()
        val eventReceived: Promise[Unit] = Promise.promise()
        var subscriptionCountBefore: Int = 0
        interactionTest(gameId, _.subscriptions.keys.size)(
          before => subscriptionCountBefore = before, {
            chessGameModel
              .subscribe[ChessGameServiceEvent](gameId, _ => eventReceived.tryComplete())
              .await
              .get
            eventReceived.future.await.get
          },
          after => after shouldEqual (subscriptionCountBefore + 1)
        )
      }
      it("should forbid the user from subscribing to an event type in a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.subscribe("NotRegisteredGameId", identity).await.get
        )
      }
    }

    describe("when cancelling a subscription to a game") {
      it("should let the user cancel a subscription to a registered game") {
        val gameId: Id = createGame()
        val subscriptionId: String =
          chessGameModel.subscribe[ChessGameServiceEvent](gameId, identity).await.get
        interactionTest(gameId, _.subscriptions.keys)(
          before => before should contain(subscriptionId),
          chessGameModel.unsubscribe(gameId, subscriptionId).await.get,
          after => after shouldNot contain(subscriptionId)
        )
      }
      it("should forbid the user from cancelling a subscription to a non-registered game") {
        assertThrows[GameNotFoundException](
          chessGameModel.unsubscribe("NotRegisteredGameId").await.get
        )
      }
    }

    describe("when a game ends") {
      it("should remove the game from the registered games") {
        val gameId: Id = makeRunningGame(createGame())
        GameRecording("F2F3", "E7E6", "G2G4")(chessGameModel.getGames.await.get.apply(gameId))
        chessGameModel.applyMove(gameId, "D8H4").await.get
        pollForProperty(!_.contains(gameId)).await
          .getOrElse(fail(s"Game '$gameId' was not deleted."))
      }
    }
  }

  after {
    this._deployment.undeploy().await.getOrElse(fail("Un-deployment failed."))
  }

  /**
   * Deploy a service using the specified [[ChessGamePort]] on the specified
   * [[Vertx]] instance.
   * @param vertx the specified [[Vertx]] instance.
   * @param implementation the specified [[ChessGamePort]].
   * @return a [[Deployment]] with which it is possible to undeploy the
   *         service.
   */
  private def deployService(vertx: Vertx, implementation: ChessGamePort): Deployment =
    DeploymentGroup
      .deploySingle(vertx) {
        new Service:
          name = "TestChessGameService"
          new Port[ChessGamePort]:
            model = implementation
      }
      .await
      .getOrElse(fail("Deployment failed."))

  /**
   * Creates a new [[ChessGameServer]] with the specified [[GameConfiguration]].
   *
   * @param gameConfiguration the specified [[GameConfiguration]].
   * @return the [[Id Id]] of the just-created [[ChessGameServer]].
   */
  private def createGame(gameConfiguration: GameConfiguration = GameConfiguration.default): Id =
    chessGameModel.createGame(gameConfiguration).await.get

  /**
   * Creates a new public [[ChessGameServer]].
   *
   * @return the [[Id Id]] of the just-created [[ChessGameServer]].
   */
  private def createPublicGame(): Id =
    createGame(GameConfiguration(isPrivate = false))

  /**
   * Creates a new private [[ChessGameServer]].
   *
   * @return the [[Id Id]] of the just-created [[ChessGameServer]].
   */
  private def createPrivateGame(): Id =
    createGame(GameConfiguration(isPrivate = true))

  /**
   * Make the [[ChessGameServer]] with the specified [[Id Id]]
   * enter the state running.
   *
   * @param gameId the specified [[Id Id]].
   * @return the specified [[Id Id]] unchanged.
   */
  private def makeRunningGame(gameId: Id): Id =
    Seq(LegacyWhitePlayer.default, LegacyBlackPlayer.default)
      .foreach(player => chessGameModel.joinGame(gameId, player).await.get)
    gameId

  /**
   * Execute a test on the [[ServerState]] of a [[ChessGameServer]] in the chess
   * game service, verifying certain properties on the [[ServerState]] before and
   * after an interaction with the [[ChessGameServer]].
   *
   * @param gameId      the id of the [[ChessGameServer]] involved in the test.
   * @param mapper      used to extract the values subjected to the test from the
   *                    [[ServerState]].
   * @param before      a consumer of the values extracted from the [[ServerState]]
   *                    before the interaction with the [[ChessGameServer]].
   * @param interaction a function performing the interaction with the [[ChessGameServer]].
   * @param after       a consumer of the values extracted from the [[ServerState]]
   *                    after the interaction with the [[ChessGameServer]].
   * @tparam S the type of the values extracted from the [[ServerState]].
   */
  private def interactionTest[S](gameId: Id, mapper: ServerState => S = identity)(
      before: S => Unit,
      interaction: => Unit,
      after: S => Unit
  ): Unit =
    before(mapper(chessGameModel.getState(gameId).await.get))
    interaction
    after(mapper(chessGameModel.getState(gameId).await.get))

  /**
   * Poll for the [[ChessGameServer]]s of the chess game service until
   * the specified property is satisfied.
   *
   * @param predicate the predicate checking for the specified property.
   * @return a [[Future]] completing when the specified property is
   *         satisfied.
   */
  private def pollForProperty(predicate: ChessGameMap => Boolean): Future[Unit] =
    chessGameModel.getGames
      .map[Boolean](games => predicate(games))
      .compose(property =>
        if property then Future.succeededFuture() else pollForProperty(predicate)
      )
