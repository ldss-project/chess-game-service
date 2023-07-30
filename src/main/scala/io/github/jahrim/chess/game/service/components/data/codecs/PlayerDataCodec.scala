package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.chess.game.service.components.data.PlayerData
import org.bson.conversions.Bson
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** [[Bson]] codec for [[PlayerData]]. */

object PlayerDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[PlayerData]]. */
  given bsonToPlayer: BsonDocumentDecoder[PlayerData] = bson =>
    PlayerData(
      bson.require("username").as[String]
    )

  /** A given [[BsonDocumentEncoder]] for [[PlayerData]]. */
  given playerToBson: BsonDocumentEncoder[PlayerData] = player =>
    bson {
      "username" :: player.username
    }
