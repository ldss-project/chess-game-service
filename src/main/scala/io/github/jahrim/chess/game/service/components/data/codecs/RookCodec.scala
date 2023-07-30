package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.{PositionData, RookData}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import RookCodec.given
import PositionCodec.given

/** [[Bson]] codec for [[RookData]]. */
object RookCodec:
  /** A given [[BsonDocumentDecoder]] for [[RookData]]. */
  given bsonToRook: BsonDocumentDecoder[RookData] = bson =>
    RookData(
      from = bson.require("from").as[PositionData],
      to = bson.require("to").as[PositionData]
    )

  /** A given [[BsonDocumentEncoder]] for [[RookData]]. */
  given RookToBson: BsonDocumentEncoder[RookData] = rook =>
    bson {
      "from" :: rook.from
      "to" :: rook.to
    }
