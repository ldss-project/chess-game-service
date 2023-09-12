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
package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson

/** [[Bson]] codecs of the chess game service. */
object Codecs:
  export ChessGameServiceExceptionCodec.given
  export StatisticsServiceExceptionCodec.given
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
