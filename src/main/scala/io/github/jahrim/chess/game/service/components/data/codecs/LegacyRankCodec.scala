package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.Rank as LegacyRank
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyRank LegacyRank]]. */
object LegacyRankCodec:
  /** A given [[BsonDecoder]] for [[LegacyRank LegacyRank]]. */
  given legacyRankDecoder: BsonDecoder[LegacyRank] = bson =>
    LegacyRank.valueOf(s"_${bson.asString.getValue}")

  /** A given [[BsonEncoder]] for [[LegacyRank LegacyRank]]. */
  given legacyRankEncoder: BsonEncoder[LegacyRank] = rank => rank.toString.asBson
