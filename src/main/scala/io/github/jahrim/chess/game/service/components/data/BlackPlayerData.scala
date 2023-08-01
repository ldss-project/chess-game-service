package io.github.jahrim.chess.game.service.components.data

import scala.concurrent.duration.Duration

case class BlackPlayerData(
                          player: PlayerData,
                          chessboard: ChessboardStatusData,
                          history: Seq[MoveData],
                          time: Duration
                          )
