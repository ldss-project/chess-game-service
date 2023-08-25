package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.File as LegacyFile
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyFile LegacyFile]]. */
object LegacyFileCodec:
  /** A given [[BsonDecoder]] for [[LegacyFile LegacyFile]]. */
  given legacyFileDecoder: BsonDecoder[LegacyFile] = bson =>
    LegacyFile.valueOf(bson.asString.getValue)

  /** A given [[BsonEncoder]] for [[LegacyFile LegacyFile]]. */
  given legacyFileEncoder: BsonEncoder[LegacyFile] = file => file.productPrefix.asBson
