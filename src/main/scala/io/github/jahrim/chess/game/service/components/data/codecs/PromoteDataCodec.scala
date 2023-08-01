package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.{PieceData, PieceTypeData, PositionData}
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[PieceData]] */
object PromoteDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[PieceData]] */
  given bsonToPiece: BsonDocumentDecoder[PieceData] = bson =>
    PieceData(
      pieceType = bson.require("type").as[PieceTypeData],
      position = bson.require("position").as[PositionData]
    )

  /** A given [[BsonDocumentEncoder]] for [[PieceData]] */
  given pieceToBson: BsonDocumentEncoder[PieceData] = piece =>
    bson {
      "type" :: piece.pieceType
      "position" :: piece.position
    }
