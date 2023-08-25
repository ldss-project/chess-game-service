package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  Player as LegacyPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyPlayer LegacyPlayer]]. */
object LegacyPlayerCodec:
  /** A given [[BsonDecoder]] for [[LegacyPlayer LegacyPlayer]]. */
  given legacyPlayerDecoder: BsonDocumentDecoder[LegacyPlayer] = bson =>
    (bson.require("team").as[LegacyTeam] match
      case LegacyTeam.WHITE => legacyWhitePlayerDecoder
      case LegacyTeam.BLACK => legacyBlackPlayerDecoder
    ).decode(bson)

  /** A given [[BsonEncoder]] for [[LegacyPlayer LegacyPlayer]]. */
  given legacyPlayerEncoder[P <: LegacyPlayer]: BsonDocumentEncoder[P] = player =>
    bson {
      "name" :: player.name
      "team" :: player.team
    }

  /** A given [[BsonDecoder]] for [[LegacyWhitePlayer LegacyWhitePlayer]]. */
  given legacyWhitePlayerDecoder: BsonDocumentDecoder[LegacyWhitePlayer] = bson =>
    LegacyWhitePlayer(bson.require("name").as[String])

  /** A given [[BsonDecoder]] for [[LegacyBlackPlayer LegacyBlackPlayer]]. */
  given legacyBlackPlayerDecoder: BsonDocumentDecoder[LegacyBlackPlayer] = bson =>
    LegacyBlackPlayer(bson.require("name").as[String])
