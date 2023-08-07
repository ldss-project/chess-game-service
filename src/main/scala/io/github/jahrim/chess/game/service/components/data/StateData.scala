package io.github.jahrim.chess.game.service.components.data

case class StateData(
    turn: TurnData,
    situation: Option[SituationData],
    white: PlayerPerspectiveData,
    black: PlayerPerspectiveData,
    gameOver: Option[GameOverData]
)
