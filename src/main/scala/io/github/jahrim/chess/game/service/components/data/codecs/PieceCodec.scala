package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.{PieceData, PieceTypeData, PositionData}
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import PieceCodec.given
import PositionCodec.given
import PieceTypeDataCodec.given
import FileCodec.given
import RankCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** [[Bson]] codec for [[PieceData]] */
object PieceCodec:

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
