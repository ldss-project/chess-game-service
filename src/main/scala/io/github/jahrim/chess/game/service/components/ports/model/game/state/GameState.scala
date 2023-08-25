package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.board.ChessBoard as LegacyChessboard
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.game.{
  ChessGameStatus as LegacyChessGameStatus,
  Team as LegacyTeam
}
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameState.ChessTimerMap
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameOver,
  GameSituation,
  MoveHistory
}

import scala.concurrent.duration.Duration

/** Adapter of a [[LegacyChessGameStatus LegacyChessGameStatus]]. */
trait GameState:
  /** The [[LegacyChessGameStatus LegacyChessGameStatus]] adapted by this [[GameState]]. */
  def legacy: LegacyChessGameStatus

  /** The [[LegacyChessboard LegacyChessboard]] of this chess game. */
  def chessboard: LegacyChessboard = legacy.chessBoard

  /** The current turn of this chess game. */
  def currentTurn: LegacyTeam = legacy.currentTurn

  /** The [[MoveHistory]] of this chess game. */
  def moveHistory: MoveHistory

  /** The [[GameConfiguration]] of this chess game. */
  def gameConfiguration: GameConfiguration

  /** The [[GameSituation]] of this chess game. */
  def gameSituation: GameSituation

  /** The [[GameOver]] status of this chess game, if any. */
  def gameOverOption: Option[GameOver]

  /** The timers of the [[LegacyPlayer Player]]s of this chess game. */
  def timers: ChessTimerMap

  /**
   * @param chessboard the specified [[LegacyChessboard LegacyChessboard]].
   * @return a new [[GameState]] obtained by updating the [[LegacyChessboard LegacyChessboard]]
   *         of this [[GameState]] to the specified [[LegacyChessboard LegacyChessboard]].
   */
  def setChessboard(chessboard: LegacyChessboard): GameState

  /**
   * @param moveHistory the specified [[MoveHistory]].
   * @return a new [[GameState]] obtained by updating the [[MoveHistory]]
   *         of this [[GameState]] to the specified [[MoveHistory]].
   */
  def setMoveHistory(moveHistory: MoveHistory): GameState

  /**
   * @param currentTurn the specified turn.
   * @return a new [[GameState]] obtained by updating the current turn
   *         of this [[GameState]] to the specified turn.
   */
  def setCurrentTurn(currentTurn: LegacyTeam): GameState

  /**
   * @param gameConfiguration the specified [[GameConfiguration]].
   * @return a new [[GameState]] obtained by updating the [[GameConfiguration]]
   *         of this [[GameState]] to the specified [[GameConfiguration]].
   */
  def setGameConfiguration(gameConfiguration: GameConfiguration): GameState

  /**
   * @param situation the specified [[GameSituation]].
   * @return a new [[GameState]] obtained by updating the [[GameSituation]]
   *         of this [[GameState]] to the specified [[GameSituation]].
   */
  def setGameSituation(situation: GameSituation): GameState

  /**
   * @param gameOver the specified [[GameOver]].
   * @return a new [[GameState]] obtained by updating the [[GameOver]] status
   *         of this [[GameState]] to the specified [[GameOver]] status.
   */
  def setGameOver(gameOver: GameOver): GameState

  /**
   * @param timers the specified timers.
   * @return a new [[GameState]] obtained by updating the timers of the
   *         [[LegacyPlayer Player]]s of this [[GameState]] to the
   *         specified timers.
   */
  def setTimers(timers: ChessTimerMap): GameState

/** Companion object of [[GameState]]. */
object GameState:
  /**
   * A map from [[LegacyTeam LegacyTeam]] to the current timer of the player
   * of that [[LegacyTeam LegacyTeam]].
   */
  type ChessTimerMap = Map[LegacyTeam, Duration]

  /** The default state of the [[LegacyChessboard LegacyChessboard]] of a chess game. */
  val DefaultChessboard: LegacyChessboard = LegacyChessboard.standard

  /** The default state of the [[MoveHistory MoveHistory]] of a chess game. */
  val DefaultHistory: MoveHistory = MoveHistory.empty

  /** The default state of the current turn of a chess game. */
  val DefaultCurrentTurn: LegacyTeam = LegacyTeam.WHITE

  /** The default state of the [[GameConfiguration]] of a chess game. */
  val DefaultGameConfiguration: GameConfiguration = GameConfiguration.default

  /** The default state of the [[GameSituation]] of a chess game. */
  val DefaultGameSituation: GameSituation = GameSituation.None

  /** The default state of the [[GameOver]] status of a chess game. */
  val DefaultGameOver: Option[GameOver] = None

  /** The default state of the timers of the [[LegacyPlayer Player]]s of a chess game. */
  val DefaultTimers: ChessTimerMap = Map()

  /** The default [[GameState]] of a chess game. */
  private val DefaultChessGameState: GameState = GameState()

  /** @return the default [[GameState]] of a chess game. */
  def default: GameState = DefaultChessGameState

  /**
   * Create a new [[GameState]].
   *
   * @param chessboard        the [[LegacyChessboard LegacyChessboard]] of the new [[GameState]].
   * @param moveHistory       the [[MoveHistory]] of the new [[GameState]].
   * @param currentTurn       the current turn of the new [[GameState]].
   * @param gameConfiguration the [[GameConfiguration]] of the new [[GameState]].
   * @param gameSituation     the [[GameSituation]] of the new [[GameState]].
   * @param gameOverOption    the [[GameOver]] of the new [[GameState]].
   * @param timers            the timers of the [[LegacyPlayer Player]]s of the new [[GameState]].
   * @return a new [[GameState]].
   */
  def apply(
      chessboard: LegacyChessboard = DefaultChessboard,
      moveHistory: MoveHistory = DefaultHistory,
      currentTurn: LegacyTeam = DefaultCurrentTurn,
      gameConfiguration: GameConfiguration = DefaultGameConfiguration,
      gameSituation: GameSituation = DefaultGameSituation,
      gameOverOption: Option[GameOver] = DefaultGameOver,
      timers: ChessTimerMap = DefaultTimers
  ): GameState =
    BasicGameState(
      LegacyChessGameStatus(chessboard, moveHistory, currentTurn, gameConfiguration.legacy),
      moveHistory,
      gameConfiguration,
      gameSituation,
      gameOverOption,
      timers
    )

  /** Basic implementation of [[GameState]]. */
  private case class BasicGameState(
      override val legacy: LegacyChessGameStatus,
      override val moveHistory: MoveHistory,
      override val gameConfiguration: GameConfiguration,
      override val gameSituation: GameSituation,
      override val gameOverOption: Option[GameOver],
      override val timers: ChessTimerMap
  ) extends GameState:
    override def setChessboard(chessboard: LegacyChessboard): BasicGameState =
      this.update(chessboard = chessboard)
    override def setMoveHistory(moveHistory: MoveHistory): BasicGameState =
      this.update(moveHistory = moveHistory)
    override def setCurrentTurn(currentTurn: LegacyTeam): BasicGameState =
      this.update(currentTurn = currentTurn)
    override def setGameConfiguration(gameConfiguration: GameConfiguration): BasicGameState =
      this.update(gameConfiguration = gameConfiguration)
    override def setGameSituation(gameSituation: GameSituation): BasicGameState =
      this.update(gameSituation = gameSituation)
    override def setGameOver(gameOver: GameOver): BasicGameState =
      this.update(gameOverOption = Some(gameOver))
    override def setTimers(timers: ChessTimerMap): BasicGameState =
      this.update(timers = timers)

    /**
     * @param chessboard        the [[LegacyChessboard LegacyChessboard]] of the new [[GameState]].
     * @param moveHistory       the [[MoveHistory]] of the new [[GameState]].
     * @param currentTurn       the current turn of the new [[GameState]].
     * @param gameConfiguration the [[GameConfiguration]] of the new [[GameState]].
     * @param gameSituation     the [[GameSituation]] of the new [[GameState]].
     * @param gameOverOption    the [[GameOver]] of the new [[GameState]].
     * @param timers            the timers of the [[LegacyPlayer Player]]s of the new [[GameState]].
     * @return a new [[BasicGameState]] obtained by updating this [[BasicGameState]] with the
     *         specified state.
     */
    private def update(
        chessboard: LegacyChessboard = this.chessboard,
        moveHistory: MoveHistory = this.moveHistory,
        currentTurn: LegacyTeam = this.currentTurn,
        gameConfiguration: GameConfiguration = this.gameConfiguration,
        gameSituation: GameSituation = this.gameSituation,
        gameOverOption: Option[GameOver] = this.gameOverOption,
        timers: ChessTimerMap = this.timers
    ): BasicGameState =
      BasicGameState(
        LegacyChessGameStatus(chessboard, moveHistory, currentTurn, gameConfiguration.legacy),
        moveHistory,
        gameConfiguration,
        gameSituation,
        gameOverOption,
        timers
      )
