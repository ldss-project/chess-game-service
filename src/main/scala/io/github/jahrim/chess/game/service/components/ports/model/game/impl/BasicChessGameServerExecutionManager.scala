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
package io.github.jahrim.chess.game.service.components.ports.model.game.impl

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.jahrim.chess.game.service.components.exceptions.*
import io.github.jahrim.chess.game.service.components.ports.model.game.impl.BasicChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameSituation,
  ServerSituation
}

/**
 * A mixin that handles the execution the activities of a
 * [[BasicChessGameServer]].
 *
 * This mixin provides modifiers to the execution of specific
 * block of codes, so that certain properties are satisfied.
 */
trait BasicChessGameServerExecutionManager:
  self: BasicChessGameServer =>

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is not configured.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameConfiguredException if this [[BasicChessGameServer]] is
   *                                 already configured.
   */
  protected def onlyIfNotConfigured[T](activity: => T): T = self.state.serverSituation match
    case ServerSituation.NotConfigured => activity
    case _                             => throw GameConfiguredException(self.id)

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is waiting for players.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameNotWaitingForPlayersException if this [[BasicChessGameServer]] is
   *                                           not waiting for players.
   */
  protected def onlyIfWaitingForPlayers[T](activity: => T): T = self.state.serverSituation match
    case ServerSituation.WaitingForPlayers => activity
    case _                                 => throw GameNotWaitingForPlayersException(self.id)

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is running.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameNotRunningException if this [[BasicChessGameServer]] is
   *                                 not running.
   */
  protected def onlyIfRunning[T](activity: => T): T = self.state.serverSituation match
    case ServerSituation.Running => activity
    case _                       => throw GameNotRunningException(self.id)

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is not terminated.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameTerminatedException if this [[BasicChessGameServer]] is
   *                                 terminated.
   */
  protected def onlyIfNotTerminated[T](activity: => T): T = self.state.serverSituation match
    case ServerSituation.Terminated => throw GameTerminatedException(self.id)
    case _                          => activity

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is ready.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameNotReadyException if this [[BasicChessGameServer]] is not ready.
   */
  protected def onlyIfReady[T](activity: => T): T = self.state.serverSituation match
    case ServerSituation.Ready => activity
    case _                     => throw GameNotReadyException(self.id)

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is waiting for a promotion.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameNotWaitingForPromotionException if this [[BasicChessGameServer]] is not
   *                                             waiting for a promotion.
   */
  protected def onlyIfWaitingForPromotion[T](activity: LegacyPosition => T): T =
    self.state.gameState.gameSituation match
      case GameSituation.Promotion(promotingPawnPosition) => activity(promotingPawnPosition)
      case _ => throw GameNotWaitingForPromotionException(self.id)

  /**
   * Executes the specified activity only if this [[BasicChessGameServer]]
   * is not waiting for a promotion.
   *
   * @param activity the specified activity.
   * @tparam T the result type of the specified activity.
   * @return the result of the specified activity.
   * @throws GameWaitingForPromotionException if this [[BasicChessGameServer]]
   *                                          is waiting for a promotion.
   */
  protected def onlyIfNotWaitingForPromotion[T](activity: => T): T =
    self.state.gameState.gameSituation match
      case GameSituation.Promotion(_) => throw GameWaitingForPromotionException(self.id)
      case _                          => activity
