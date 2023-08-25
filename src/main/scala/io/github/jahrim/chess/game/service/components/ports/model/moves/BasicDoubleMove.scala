package io.github.jahrim.chess.game.service.components.ports.model.moves

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.chess.engine.model.moves.DoubleMove as LegacyDoubleMove

/**
 * Basic implementation of a [[LegacyDoubleMove LegacyDoubleMove]].
 *
 * @param from the starting [[LegacyPosition LegacyPosition]].
 * @param to the arriving [[LegacyPosition LegacyPosition]].
 * @param middlePosition the [[LegacyPosition LegacyPosition]] between
 *                       the starting and arriving
 *                       [[LegacyPosition LegacyPosition]]s.
 */
case class BasicDoubleMove(
    from: LegacyPosition,
    to: LegacyPosition,
    middlePosition: LegacyPosition
) extends LegacyDoubleMove
