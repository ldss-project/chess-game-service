package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyTeam LegacyTeam]]. */
object LegacyTeamCodec:
  /** A given [[BsonDecoder]] for [[LegacyTeam LegacyTeam]]. */
  given legacyTeamDecoder: BsonDecoder[LegacyTeam] = bson =>
    LegacyTeam.valueOf(bson.asString.getValue)

  /** A given [[BsonEncoder]] for [[LegacyTeam LegacyTeam]]. */
  given legacyTeamEncoder: BsonEncoder[LegacyTeam] = team => team.toString.asBson
