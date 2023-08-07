package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.FindMovesDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[FindMovesData]] */
object FindMovesDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[FindMovesData]] */
  given bsonToFindMoves: BsonDocumentDecoder[FindMovesData] = bson =>
    FindMovesData(
      position = bson.require("position").as[PositionData]
    )

  /** A given [[BsonDocumentEncoder]] for [[FindMovesData]] */
  given findMovesToBson: BsonDocumentEncoder[FindMovesData] = m =>
    bson {
      "position" :: m.position
    }
