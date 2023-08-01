package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[MoveData]]. */
object WhitePlayerDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[MoveData]]. */
  given bsonToMove: BsonDocumentDecoder[MoveData] = bson =>
    MoveData(
      typeMove = bson.require("type").as[MoveTypeData],
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
