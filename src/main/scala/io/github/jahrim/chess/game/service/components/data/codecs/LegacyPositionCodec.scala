package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{
  File as LegacyFile,
  Position as LegacyPosition,
  Rank as LegacyRank
}
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyFileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyRankCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyPosition LegacyPosition]]. */
object LegacyPositionCodec:
  /** A given [[BsonDecoder]] for [[LegacyPosition LegacyPosition]]. */
  given legacyPositionDecoder: BsonDocumentDecoder[LegacyPosition] = bson =>
    LegacyPosition(
      bson.require("file").as[LegacyFile],
      bson.require("rank").as[LegacyRank]
    )

  /** A given [[BsonEncoder]] for [[LegacyPosition LegacyPosition]]. */
  given legacyPositionEncoder: BsonDocumentEncoder[LegacyPosition] = position =>
    bson {
      "file" :: position.file
      "rank" :: position.rank
    }
