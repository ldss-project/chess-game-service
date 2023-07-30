package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.{CastlingData, PositionData, RookData}
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import PositionDataCodec.given

/** [[Bson]] codec for [[CastlingData]]. */
object CastlingDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[CastlingData]]. */
  given bsonToCastling: BsonDocumentDecoder[CastlingData] = bson =>
    CastlingData(
      rookFrom = bson.require("rook.from").as[PositionData],
      rookTo = bson.require("rook.to").as[PositionData]
    )

  /** A given [[BsonDocumentEncoder]] for [[CastlingData]]. */
  given castlingToBson: BsonDocumentEncoder[CastlingData] = c =>
    bson {
      "rook" :# {
        "from" :: c.rookFrom
        "to" :: c.rookTo
      }
    }
