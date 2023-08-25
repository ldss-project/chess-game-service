package io.github.jahrim.chess.game.service.components.ports

import io.github.chess.engine.model.board.{
  File as LegacyFile,
  Position as LegacyPosition,
  Rank as LegacyRank
}
import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.jahrim.chess.game.service.components.exceptions.{
  GameNotReadyException,
  GameNotRunningException,
  GameNotWaitingForPromotionException
}
import io.github.jahrim.chess.game.service.components.ports.GameRecording.*
import io.github.jahrim.chess.game.service.components.ports.model.game.ChessGameServer
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameConfiguration,
  PromotionChoice
}
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.{*, given}
import io.vertx.core.Vertx

/**
 * A recording of the [[Action Action]]s taken by the players during a [[ChessGameServer]].
 *
 * @param actions the [[Action Action]]s taken by the players during the [[ChessGameServer]].
 */
case class GameRecording(actions: Action*):
  /**
   * Create a [[ChessGameServer]] with the specified [[GameConfiguration]] and deployed
   * on the specified [[Vertx]] instance, applying all the [[Action Action]]s saved
   * in this [[GameRecording]], thus bringing the just-created [[ChessGameServer]] to the
   * last state recorded by this [[GameRecording]].
   *
   * @param vertx         the specified [[Vertx]] instance.
   * @param configuration the specified [[GameConfiguration]].
   * @return a new [[ChessGameServer]] up-to-date with the last state recorded by this
   *         [[GameRecording]].
   * @throws GameNotReadyException if the specified [[GameConfiguration]] is incomplete.
   */
  def createGame(vertx: Vertx, configuration: GameConfiguration): ChessGameServer =
    val chessGame = ChessGameServer(vertx)
    chessGame.configure(configuration).await.get
    chessGame.start().await.get
    this.apply(chessGame)
    chessGame

  /**
   * Apply all the [[Action Action]]s saved in this [[GameRecording]] to the specified
   * [[ChessGameServer]].
   *
   * @param chessGame the specified [[ChessGameServer]].
   * @throws GameNotRunningException             if the specified [[ChessGameServer]] is not already running or the
   *                                             recordings are faulty.
   * @throws GameNotWaitingForPromotionException if the specified [[ChessGameServer]] is not already
   *                                             waiting for promotion or the recordings are faulty.
   */
  def apply(chessGame: ChessGameServer): Unit =
    actions.foreach(action => {
      action match
        case move: LegacyMove                 => chessGame.applyMove(move).await.get
        case promotionChoice: PromotionChoice => chessGame.promote(promotionChoice).await.get
    })

/** Companion object of [[GameRecording]]. */
object GameRecording:
  /**
   * An action taken by a player during a [[ChessGameServer]].
   *
   * An [[Action Action]] is either a:
   *  - [[LegacyMove LegacyMove]]: if a piece was moved;
   *  - [[PromotionChoice]]: if a pawn was promoted.
   */
  type Action = LegacyMove | PromotionChoice

  /**
   * A [[GameRecording]] of a [[ChessGameServer]] where no actions
   * have been taken by the participating players yet.
   */
  val JustStarted: GameRecording = GameRecording()

  /**
   * A given [[Conversion]] from [[String]] to [[LegacyPosition LegacyPosition]].
   *
   * @note the [[String]] must conform to the format `FR` where:
   *        - F is a [[LegacyFile LegacyFile]] in the chars {A, B, C, D, E, F, G, H}
   *        - R is a [[LegacyRank LegacyRank]] in the chars {1, 2, 3, 4, 5, 6, 7, 8}
   * @example {{{
   *   val position: LegacyPosition = "B8"
   *   println(position == LegacyPosition(LegacyFile.B, LegacyRank._8))
   *   // Output: true
   * }}}
   * @throws IllegalArgumentException if the [[String]] does not conform to the specified format.
   */
  given stringToPosition: Conversion[String, LegacyPosition] = positionString =>
    if positionString.length == 2 then
      LegacyPosition(
        LegacyFile.valueOf(positionString.charAt(0).toString),
        LegacyRank.valueOf(s"_${positionString.charAt(1).toString}")
      )
    else
      throw IllegalArgumentException(
        s"Could not convert string '$positionString' due to invalid length."
      )

  /**
   * A given [[Conversion]] from [[String]] to [[LegacyMove LegacyMove]].
   *
   * @note the [[String]] must conform to the format `PP` where:
   *        - P is a [[LegacyPosition]] conforming to the format as in [[stringToPosition]].
   * @example {{{
   *   val move: LegacyMove = "B2B3"
   *   println(move == LegacyMove(
   *     from = LegacyPosition(LegacyFile.B, LegacyRank._2),
   *     to = LegacyPosition(LegacyFile.B, LegacyRank._3)
   *   ))
   *   // Output: true
   * }}}
   * @throws IllegalArgumentException if the [[String]] does not conform to the specified format.
   */
  given stringToMove: Conversion[String, LegacyMove] = moveString =>
    LegacyMove(from = moveString.substring(0, 2), to = moveString.substring(2, 4))
