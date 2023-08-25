package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.moves.Move as LegacyMove

/** A situation in a chess game. */
enum GameSituation:
  /** The [[GameSituation]] where nothing happened in particular. */
  case None

  /**
   * The [[GameSituation]] where the king of the [[LegacyPlayer Player]]
   * of the current turn is threatened by a piece of the opponent.
   */
  case Check

  /**
   * The [[GameSituation]] where the [[LegacyPlayer Player]] of the
   * current turn has no available [[LegacyMove Move]]s.
   */
  case Stale

  /**
   * The [[GameSituation]] where both [[GameSituation.Check]]
   * and [[GameSituation.Stale]] hold.
   */
  case Checkmate

  /**
   * The [[GameSituation]] where the [[LegacyPlayer Player]] of the
   * current turn is promoting a pawn.
   */
  case Promotion(promotingPawn: LegacyPosition)
