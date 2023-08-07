package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PromoteToDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.{
  PieceData,
  PieceTypeData,
  PositionData,
  PromoteData,
  PromoteToData
}
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[PromoteData]] */
object PromoteDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[PromoteData]] */
  given bsonToPromote: BsonDocumentDecoder[PromoteData] = bson =>
    PromoteData(
      pawn = bson.require("pawn").as[PieceData],
      to = bson.require("to").as[PromoteToData]
    )

  /** A given [[BsonDocumentEncoder]] for [[PromoteData]] */
  given promoteToBson: BsonDocumentEncoder[PromoteData] = p =>
    bson {
      "pawn" :: p.pawn
      "to" :: p.to
    }
