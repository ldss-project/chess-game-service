package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.ApplyMoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ApplyMoveData]] */
object ApplyMoveDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[ApplyMoveData]] */
  given bsonToApplyMove: BsonDocumentDecoder[ApplyMoveData] = bson =>
    ApplyMoveData(
      move = bson.require("move").as[MoveData]
    )

  /** A given [[BsonDocumentEncoder]] for [[ApplyMoveData]] */
  given applyMoveToBson: BsonDocumentEncoder[ApplyMoveData] = m =>
    bson {
      "move" :: m.move
    }
