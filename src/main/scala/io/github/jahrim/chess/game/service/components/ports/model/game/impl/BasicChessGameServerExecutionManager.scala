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
