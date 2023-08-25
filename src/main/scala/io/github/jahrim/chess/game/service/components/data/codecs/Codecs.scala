package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson

/** [[Bson]] codecs of the chess game service. */
object Codecs:
  export ChessGameServiceExceptionCodec.given
  export ChessTimerMapCodec.given
  export DurationCodec.given
  export EventCodec.given
  export EventWithPayloadCodec.given
  export GameConfigurationCodec.given
  export GameOverCodec.given
  export GameSituationCodec.given
  export GameStateCodec.given
  export MoveHistoryCodec.given
  export PromotionChoiceCodec.given
  export ServerSituationCodec.given
  export ServerStateCodec.given
  export OptionCodec.given
  export JsonObjectCodec.given
  export LegacyChessboardCodec.given
  export LegacyFileCodec.given
  export LegacyGameModeCodec.given
  export LegacyGameOverCauseCodec.given
  export LegacyMoveCodec.given
  export LegacyPieceCodec.given
  export LegacyPlayerCodec.given
  export LegacyPositionCodec.given
  export LegacyRankCodec.given
  export LegacyTeamCodec.given
  export LegacyTimeConstraintCodec.given
