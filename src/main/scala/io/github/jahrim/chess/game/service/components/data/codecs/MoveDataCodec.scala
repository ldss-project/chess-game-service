package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.{
  CastlingData,
  EnPassantData,
  MoveData,
  MoveTypeData,
  PositionData
}
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import org.bson.conversions.Bson
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import MoveTypeCodec.given
import MoveDataCodec.given
import PositionCodec.given
import CastlingCodec.given
import EnPassantCodec.given

/** [[Bson]] codec for [[MoveData]]. */
object MoveDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[MoveData]]. */
  given bsonToMove: BsonDocumentDecoder[MoveData] = bson =>
    MoveData(
      typeMove = bson.require("move").as[MoveTypeData],
      from = bson.require("from").as[PositionData],
      to = bson.require("to").as[PositionData],
      castling = bson("castling").map(_.as[CastlingData]),
      enPassant = bson("enPassant").map(_.as[EnPassantData])
    )

  /** A given [[BsonDocumentEncoder]] for [[MoveData]]. */
  given moveToBson: BsonDocumentEncoder[MoveData] = m =>
    bson {
      "type" :: m.typeMove
      "from" :: m.from
      "to" :: m.to
      m.castling.foreach { "castling" :: _ }
      m.enPassant.foreach { "enPassant" :: _ }
    }
