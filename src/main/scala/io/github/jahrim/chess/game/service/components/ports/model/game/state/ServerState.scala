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
package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.game.{
  ChessGameHistory as LegacyChessGameHistory,
  ChessGameStatus as LegacyChessGameStatus,
  Team as LegacyTeam
}
import io.github.jahrim.chess.game.service.components.exceptions.ChessGameServiceException
import io.github.jahrim.chess.game.service.components.ports.model.game.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameState.ChessTimerMap
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState.*
import io.vertx.core.eventbus.MessageConsumer

/** The state of a server hosting a chess game. */
trait ServerState:
  /** The current [[ServerSituation]] of the server hosting this chess game. */
  def serverSituation: ServerSituation

  /** The latest error of the server hosting this chess game, if any. */
  def serverErrorOption: Option[ChessGameServiceException]

  /** The subscriptions to the server hosting this chess game. */
  def subscriptions: SubscriptionMap

  /** The [[GameState]] to the server hosting this chess game. */
  def gameState: GameState

  /**
   * @param serverSituation the specified [[ServerSituation ServerSituation]].
   * @return a new [[ServerState]] obtained by updating the [[ServerSituation]]
   *         of this [[ServerState]] to the specified [[ServerSituation]].
   */
  def setServerSituation(serverSituation: ServerSituation): ServerState

  /**
   * @param serverErrorOption the specified error, if any.
   * @return a new [[ServerState]] obtained by updating the latest error
   *         of this [[ServerState]] to the specified error, if any.
   */
  def setServerError(serverErrorOption: Option[ChessGameServiceException]): ServerState

  /**
   * @param subscriptions the specified subscriptions.
   * @return a new [[ServerState]] obtained by updating the subscriptions
   *         of this [[ServerState]] to the specified subscriptions.
   */
  def setSubscriptions(subscriptions: SubscriptionMap): ServerState

  /**
   * @param gameState the specified [[GameState]].
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] to the specified [[GameState]].
   */
  def setGameState(gameState: GameState): ServerState

  /**
   * @param chessboard the specified [[LegacyChessboard LegacyChessboard]].
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setChessboard]].
   */
  def setChessboard(chessboard: LegacyChessboard): ServerState =
    this.setGameState(this.gameState.setChessboard(chessboard))

  /**
   * @param moveHistory the specified [[MoveHistory]].
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setMoveHistory]].
   */
  def setMoveHistory(moveHistory: MoveHistory): ServerState =
    this.setGameState(this.gameState.setMoveHistory(moveHistory))

  /**
   * @param currentTurn the specified current turn.
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setCurrentTurn]].
   */
  def setCurrentTurn(currentTurn: LegacyTeam): ServerState =
    this.setGameState(this.gameState.setCurrentTurn(currentTurn))

  /**
   * @param gameConfiguration the specified [[GameConfiguration]].
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setGameConfiguration]].
   */
  def setGameConfiguration(gameConfiguration: GameConfiguration): ServerState =
    this.setGameState(this.gameState.setGameConfiguration(gameConfiguration))

  /**
   * @param gameSituation the specified [[GameSituation]].
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setGameSituation]].
   */
  def setGameSituation(gameSituation: GameSituation): ServerState =
    this.setGameState(this.gameState.setGameSituation(gameSituation))

  /**
   * @param gameOver the specified [[GameOver]] status.
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setGameOver]].
   */
  def setGameOver(gameOver: GameOver): ServerState =
    this.setGameState(this.gameState.setGameOver(gameOver))

  /**
   * @param timers the specified timers.
   * @return a new [[ServerState]] obtained by updating the [[GameState]]
   *         of this [[ServerState]] as for [[GameState.setTimers]].
   */
  def setTimers(timers: ChessTimerMap): ServerState =
    this.setGameState(this.gameState.setTimers(timers))

/** Companion object of [[ServerState]]. */
object ServerState:
  /** A map from subscription [[Id]]s to subscription consumers. */
  type SubscriptionMap = Map[Id, MessageConsumer[_]]

  /** An identifier for an entity. */
  type Id = String

  /** The default state for the [[ServerSituation]] of a server hosting a chess game. */
  val DefaultServerSituation: ServerSituation = ServerSituation.NotConfigured

  /** The default state for the latest error of a server hosting a chess game. */
  val DefaultServerError: Option[ChessGameServiceException] = None

  /** The default state for the subscriptions of a server hosting a chess game. */
  val DefaultSubscriptions: SubscriptionMap = Map()

  /** The default state for the [[GameState]] of a server hosting a chess game. */
  val DefaultGameState: GameState = GameState.default

  /** The default [[ServerState]] for a server hosting a chess game. */
  private val DefaultServerState: ServerState = ServerState()

  /**
   * Create a new [[ServerState]].
   *
   * @param serverSituation   the [[ServerSituation]] of the new [[ServerState]].
   * @param serverErrorOption the latest [[ChessGameServiceException]] of the new [[ServerState]].
   * @param subscriptions     the subscriptions of the new [[ServerState]].
   * @param gameState         the [[GameState]] of the new [[ServerState]].
   * @return a new [[ServerState]].
   */
  def apply(
      serverSituation: ServerSituation = DefaultServerSituation,
      serverErrorOption: Option[ChessGameServiceException] = DefaultServerError,
      subscriptions: SubscriptionMap = DefaultSubscriptions,
      gameState: GameState = DefaultGameState
  ): ServerState =
    BasicServerState(serverSituation, serverErrorOption, subscriptions, gameState)

  /** @return the default [[ServerState]] for a server hosting a chess game. */
  def default: ServerState = DefaultServerState

  /** Basic implementation of [[ServerState]]. */
  private case class BasicServerState(
      override val serverSituation: ServerSituation,
      override val serverErrorOption: Option[ChessGameServiceException],
      override val subscriptions: SubscriptionMap,
      override val gameState: GameState
  ) extends ServerState:
    override def setServerSituation(serverSituation: ServerSituation): BasicServerState =
      this.update(serverSituation = serverSituation)
    override def setServerError(
        serverErrorOption: Option[ChessGameServiceException]
    ): BasicServerState =
      this.update(serverErrorOption = serverErrorOption)
    override def setSubscriptions(subscriptions: SubscriptionMap): BasicServerState =
      this.update(subscriptions = subscriptions)
    override def setGameState(gameState: GameState): BasicServerState =
      this.update(gameState = gameState)

    /**
     * @param serverSituation   the [[ServerSituation]] of the new [[ServerState]].
     * @param serverErrorOption the latest [[ChessGameServiceException]] of the new [[ServerState]].
     * @param subscriptions     the subscriptions of the new [[ServerState]].
     * @param gameState         the [[GameState]] of the new [[ServerState]].
     * @return a new [[BasicServerState]] obtained by updating this [[BasicServerState]] with the
     *         specified state.
     */
    private def update(
        serverSituation: ServerSituation = this.serverSituation,
        serverErrorOption: Option[ChessGameServiceException] = this.serverErrorOption,
        subscriptions: SubscriptionMap = this.subscriptions,
        gameState: GameState = this.gameState
    ): BasicServerState =
      BasicServerState(serverSituation, serverErrorOption, subscriptions, gameState)
