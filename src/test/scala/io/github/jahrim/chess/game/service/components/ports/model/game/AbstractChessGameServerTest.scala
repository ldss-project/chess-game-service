package io.github.jahrim.chess.game.service.components.ports.model.game

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.jahrim.chess.game.service.components.events.ChessGameServiceEvent
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  ServerState
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}
import io.vertx.core.{Promise, Vertx}
import test.AbstractTest

/**
 * A test for [[ChessGameServer]].
 *
 * @param chessGameSupplier a supplier for [[ChessGameServer]]s. This will be called at
 *                          the beginning of each unit test to initialize a new
 *                          instance of the [[ChessGameServer]] the test will be applied
 *                          to.
 */
abstract class AbstractChessGameServerTest(chessGameSupplier: Vertx => ChessGameServer)
    extends AbstractTest:
  private val _vertx: Vertx = Vertx.vertx()
  private var _chessGameServer: ChessGameServer = chessGameSupplier(this._vertx)

  before {
    this._chessGameServer = chessGameSupplier(this._vertx)
  }

  after {
    this._chessGameServer.stop().await.get
  }

  /**
   * @return the current instance of [[ChessGameServer]]. This will be reset at the
   *         beginning of each unit test using the supplier provided when extending
   *         this [[AbstractChessGameServerTest]].
   */
  protected def chessGameServer: ChessGameServer = this._chessGameServer

  /**
   * Execute a test on the [[ServerState]] of a [[ChessGameServer]], verifying certain
   * properties on the [[ServerState]] before and after an interaction with the
   * [[ChessGameServer]].
   *
   * @param mapper      used to extract the values subjected to the test from the
   *                    [[ServerState]].
   * @param before      a consumer of the values extracted from the [[ServerState]]
   *                    before the interaction with the [[ChessGameServer]].
   * @param interaction a function performing the interaction with the [[ChessGameServer]].
   * @param after       a consumer of the values extracted from the [[ServerState]]
   *                    after the interaction with the [[ChessGameServer]].
   * @tparam S the type of the values extracted from the [[ServerState]].
   */
  protected def interactionTest[S](mapper: ServerState => S = identity)(
      before: S => Unit,
      interaction: => Unit,
      after: S => Unit
  ): Unit =
    before(mapper(chessGameServer.getState.await.get))
    interaction
    after(mapper(chessGameServer.getState.await.get))

  protected def stopGameTest(): Unit =
    interactionTest(_.serverSituation)(
      before => before shouldNot be(Terminated),
      chessGameServer.stop().await.get,
      after => after shouldBe Terminated
    )

  protected def subscribeTest(): Unit =
    val eventReceived: Promise[Unit] = Promise.promise()
    var subscriptionId: String = ""
    interactionTest(_.subscriptions)(
      before => before.keys shouldBe empty, {
        subscriptionId = chessGameServer
          .subscribe[ChessGameServiceEvent](_ => eventReceived.tryComplete())
          .await
          .get
        eventReceived.future.await.get
      },
      after => after.keys should contain(subscriptionId)
    )

  protected def unsubscribeTest(): Unit =
    val subscriptionId: String =
      chessGameServer.subscribe[ChessGameServiceEvent](identity).await.get
    interactionTest(_.subscriptions)(
      before => before.keys should contain(subscriptionId),
      chessGameServer.unsubscribe(subscriptionId).await.get,
      after => after.keys shouldNot contain(subscriptionId)
    )

/** Companion object of [[AbstractChessGameServerTest]]. */
object AbstractChessGameServerTest:
  /** A supplier of not configured [[ChessGameServer]]s. */
  val ChessGameNotConfiguredSupplier: Vertx => ChessGameServer =
    vertx => ChessGameServer(vertx)

  /** A supplier of [[ChessGameServer]]s waiting for players. */
  val ChessGameWaitingForPlayersSupplier: Vertx => ChessGameServer =
    vertx => {
      val chessGame: ChessGameServer = ChessGameNotConfiguredSupplier(vertx)
      chessGame.configure(GameConfiguration(gameId = "ChessGameTest")).await.get
      chessGame
    }

  /** A supplier of [[ChessGameServer]]s ready to start. */
  val ChessGameReadySupplier: Vertx => ChessGameServer =
    vertx => {
      val chessGame: ChessGameServer = ChessGameWaitingForPlayersSupplier(vertx)
      chessGame.join(LegacyWhitePlayer.default).await.get
      chessGame.join(LegacyBlackPlayer.default).await.get
      chessGame
    }

  /** A supplier of running [[ChessGameServer]]s. */
  val ChessGameRunningSupplier: Vertx => ChessGameServer =
    vertx => {
      val chessGame: ChessGameServer = ChessGameReadySupplier(vertx)
      chessGame.start().await.get
      chessGame
    }

  /** A supplier of terminated [[ChessGameServer]]s. */
  val ChessGameTerminatedSupplier: Vertx => ChessGameServer =
    vertx => {
      val chessGame: ChessGameServer = ChessGameRunningSupplier(vertx)
      chessGame.stop().await.get
      chessGame
    }
