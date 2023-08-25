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
