package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.GameMode as LegacyGameMode
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyGameMode LegacyGameMode]]. */
object LegacyGameModeCodec:
  /** A given [[BsonDecoder]] for [[LegacyGameMode LegacyGameMode]]. */
  given legacyGameModeDecoder: BsonDecoder[LegacyGameMode] = bson =>
    LegacyGameMode.valueOf(bson.asString.getValue)

  /** A given [[BsonEncoder]] for [[LegacyGameMode LegacyGameMode]]. */
  given legacyGameModeEncoder: BsonEncoder[LegacyGameMode] = gameMode => gameMode.toString.asBson
