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

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  GameMode as LegacyGameMode,
  TimeConstraint as LegacyTimeConstraint,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyGameModeCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPlayerCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTimeConstraintCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameConfiguration
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState.Id
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[GameConfiguration]]. */
object GameConfigurationCodec:
  /** A given [[BsonDecoder]] for [[GameConfiguration]]. */
  given gameConfigurationDecoder: BsonDocumentDecoder[GameConfiguration] = bson =>
    GameConfiguration(
      timeConstraint = bson("timeConstraint")
        .map(_.as[LegacyTimeConstraint])
        .getOrElse(GameConfiguration.DefaultTimeConstraint),
      gameMode = bson("gameMode")
        .map(_.as[LegacyGameMode])
        .getOrElse(GameConfiguration.DefaultGameMode),
      whitePlayer = bson("whitePlayer")
        .flatMap(_.as[Option[LegacyWhitePlayer]])
        .getOrElse(GameConfiguration.DefaultWhitePlayer),
      blackPlayer = bson("blackPlayer")
        .flatMap(_.as[Option[LegacyBlackPlayer]])
        .getOrElse(GameConfiguration.DefaultBlackPlayer),
      gameId = bson("gameId")
        .flatMap(_.as[Option[Id]])
        .getOrElse(GameConfiguration.DefaultGameId),
      isPrivate = bson("isPrivate")
        .map(_.as[Boolean])
        .getOrElse(GameConfiguration.DefaultIsPrivate)
    )

  /** A given [[BsonEncoder]] for [[GameConfiguration]]. */
  given gameConfigurationEncoder: BsonDocumentEncoder[GameConfiguration] = gameConfiguration =>
    bson {
      "timeConstraint" :: gameConfiguration.timeConstraint
      "gameMode" :: gameConfiguration.gameMode
      "whitePlayer" :: gameConfiguration.playerOption(LegacyTeam.WHITE)
      "blackPlayer" :: gameConfiguration.playerOption(LegacyTeam.BLACK)
      "gameId" :: gameConfiguration.gameIdOption
      "isPrivate" :: gameConfiguration.isPrivate
    }
