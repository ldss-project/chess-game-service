package io.github.jahrim.chess.game.service.components.ports.model.game.impl

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.game.ChessGameAnalyzer.ChessGameSituation as LegacyChessGameSituation
import io.github.chess.engine.model.game.{
  ChessGameAnalyzer as LegacyChessGameAnalyzer,
  GameOverCause as LegacyGameOverCause,
  Team as LegacyTeam,
  TimerManager as LegacyTimerManager
}
import io.github.chess.engine.model.moves.{
  CastlingMove as LegacyCastlingMove,
  EnPassantMove as LegacyEnPassantMove,
  Move as LegacyMove
}
import io.github.jahrim.chess.game.service.components.events.ServerSituationUpdateEvent
import io.github.jahrim.chess.game.service.components.exceptions.PlayerAlreadyExistingException
import io.github.jahrim.chess.game.service.components.ports.model.game.ChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.impl.{
  BasicChessGameServerEventManager,
  BasicChessGameServerExecutionManager
}
import io.github.jahrim.chess.game.service.components.ports.model.game.state.*
import io.github.jahrim.chess.game.service.util.activity.{ActivityLogging, LoggingFunction}
import io.github.jahrim.chess.game.service.util.vertx.FutureExtension.*
import io.github.jahrim.hexarc.logging.{LoggerFactory, Logging}
import io.vertx.core.{Future, Vertx}
import org.slf4j.Logger

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

/**
 * Basic implementation of a [[ChessGameServer]].
 *
 * @param vertx the [[Vertx]] instance where this [[ChessGameServer]]
 *              will be deployed.
 */
class BasicChessGameServer(val vertx: Vertx)
    extends ChessGameServer
    with BasicChessGameServerExecutionManager
    with BasicChessGameServerEventManager
    with Logging
    with ActivityLogging:
  /**
   * The [[Vertx]] instance where the asynchronous activities of this
   * server will be executed.
   */
  protected given Vertx = vertx

  protected var _state: ServerState = ServerState.default
  private val _timerManager: LegacyTimerManager = LegacyTimerManager()
  private var _logger: Logger = BasicChessGameServer.Logger

  // Initialize all the Prolog Rule(s)
  assert(!LegacyChessGameAnalyzer.check(state.gameState.legacy))

  override protected def logger: Logger = this._logger
  override protected def defaultActivityLogger: LoggingFunction = logMessage << _

  override def state: ServerState = this._state

  override def getState: Future[ServerState] =
    asyncActivity("Retrieve state") { state }

  override def configure(gameConfiguration: GameConfiguration): Future[Unit] =
    asyncActivity(s"Configure game") {
      onlyIfNotConfigured {
        this._state = this._state.setGameConfiguration(gameConfiguration)
        if this.hasGameId then
          this._logger = LoggerFactory.fromName(s"${this.getClass.getSimpleName}#${this.id}")
          serverSituation << (
            if this.hasEnoughPlayers
            then ServerSituation.Ready
            else ServerSituation.WaitingForPlayers
          )
      }
    }.onFailure(failure => serverError << failure)

  override def join(newPlayer: LegacyPlayer): Future[Unit] =
    asyncActivity("Join game")(
      onlyIfWaitingForPlayers {
        configuration.playerOption(newPlayer.team) match
          case Some(existingPlayer) => throw PlayerAlreadyExistingException(id, newPlayer.team)
          case None                 => player << newPlayer
      }
    ).followedBy(_ => if this.hasEnoughPlayers then serverSituation << ServerSituation.Ready)
      .onFailure(failure => serverError << failure)

  override def start(): Future[Unit] =
    asyncActivity("Start game")(
      onlyIfReady {
        this._timerManager.start(
          state.gameState.gameConfiguration.timeConstraint,
          this._timerManager
            .currentTimer(state.gameState.currentTurn)
            .foreach(currentTimer =>
              timer << (
                state.gameState.currentTurn,
                Duration(
                  currentTimer.timeRemaining.seconds,
                  TimeUnit.SECONDS
                )
              )
            ),
          gameOver << GameOver(
            LegacyGameOverCause.Timeout,
            state.gameState.gameConfiguration.playerOption(
              state.gameState.currentTurn.oppositeTeam
            )
          )
        )
        serverSituation << ServerSituation.Running
      }
    ).onFailure(failure => serverError << failure)

  override def stop(): Future[Unit] =
    asyncActivity("Stop game")(
      onlyIfNotTerminated {
        LegacyTeam.values.foreach(team => this._timerManager.stop(team))
        serverSituation << ServerSituation.Terminated
        vertx.runOnContext(_ => forcedUnsubscribe(state.subscriptions.keys.toSeq*))
      }
    ).onFailure(failure => serverError << failure)

  override def findMoves(position: LegacyPosition): Future[Set[LegacyMove]] =
    asyncActivity(s"Find moves at position '$position'")(
      onlyIfRunning {
        onlyIfNotWaitingForPromotion {
          state.gameState.chessboard
            .pieces(state.gameState.currentTurn)
            .get(position)
            .map(piece => piece.rule.findMoves(position, state.gameState.legacy))
            .getOrElse(Set.empty)
        }
      }
    ).onFailure(failure => serverError << failure)

  override def applyMove(move: LegacyMove): Future[Unit] =
    asyncActivity(s"Apply move '$move'")(
      onlyIfRunning {
        onlyIfNotWaitingForPromotion {
          chessboard << {
            val chessBoard = state.gameState.chessboard.movePiece(move.from, move.to)
            move match
              case castlingMove: LegacyCastlingMove =>
                chessBoard.movePiece(castlingMove.rookFromPosition, castlingMove.rookToPosition)
              case enPassantMove: LegacyEnPassantMove =>
                chessBoard.removePiece(enPassantMove.capturedPiecePosition)
              case _ => chessBoard
          }
          moveHistory << {
            state.gameState.chessboard.pieces.get(move.to) match
              case Some(piece) => state.gameState.moveHistory.save(piece, move)
              case None        => state.gameState.moveHistory
          }

          LegacyChessGameAnalyzer.promotion(state.gameState.legacy) match
            case Some(promotingPawnPosition) =>
              this._timerManager.stop(state.gameState.currentTurn)
              gameSituation << GameSituation.Promotion(promotingPawnPosition)
            case _ =>
              switchTurn()
        }
      }
    ).onFailure(failure => serverError << failure)

  override def promote(promotionChoice: PromotionChoice): Future[Unit] =
    asyncActivity(s"Promote pawn to '$promotionChoice")(
      onlyIfWaitingForPromotion { promotingPawnPosition =>
        chessboard <<
          state.gameState.chessboard.setPiece(
            promotingPawnPosition,
            promotionChoice.asPieceOfTeam(state.gameState.currentTurn)
          )
        switchTurn()
      }
    ).onFailure(failure => serverError << failure)

  /** Change the player currently in control of the chessboard in this chess game. */
  private def switchTurn(): Unit =
    activity("Switch Turn")(
      onlyIfRunning {
        this._timerManager.restart(state.gameState.currentTurn)
        currentTurn << state.gameState.currentTurn.oppositeTeam
        analyzeChessboard()
      }
    )

  /** Analyze the chessboard searching for any particular situation. */
  private def analyzeChessboard(): Unit =
    activity("Analyze chessboard")(
      onlyIfRunning {
        LegacyChessGameAnalyzer.situationOf(state.gameState.legacy) match
          case Some(LegacyChessGameSituation.CheckMate) =>
            gameSituation << GameSituation.Checkmate
            gameOver << GameOver(
              LegacyGameOverCause.Checkmate,
              state.gameState.gameConfiguration.playerOption(
                state.gameState.currentTurn.oppositeTeam
              )
            )
          case Some(LegacyChessGameSituation.Stale) =>
            gameSituation << GameSituation.Stale
            gameOver << GameOver(LegacyGameOverCause.Stalemate)
          case Some(LegacyChessGameSituation.Check) =>
            gameSituation << GameSituation.Check
          case _ =>
            gameSituation << GameSituation.None
      }
    )

/** Companion object of [[BasicChessGameServer]]. */
object BasicChessGameServer:
  /** Default logger for a [[BasicChessGameServer]]. */
  private val Logger = LoggerFactory.fromClass(classOf[BasicChessGameServer], false)
