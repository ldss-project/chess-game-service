package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.EnPassantData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.chess.game.service.components.data.PieceData
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import PieceCodec.given
import EnPassantData.given

/** [[Bson]] codec for [[EnPassantData]]. */
object EnPassantCodec:
  /** A given [[BsonDocumentDecoder]] for [[EnPassantData]]. */
  given bsonToEnPassant: BsonDocumentDecoder[EnPassantData] = bson =>
    EnPassantData(
      opponentPawn = bson.require("opponentPawn").as[PieceData]
    )

  /** A given [[BsonDocumentEncoder]] for [[EnPassantData]]. */
  given enPassantToBson: BsonDocumentEncoder[EnPassantData] = e =>
    bson {
      "opponentPawn" :: e.opponentPawn
    }
