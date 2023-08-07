package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.InputDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FindMovesDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ApplyMoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PromoteDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[InputData]] */
object InputDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[InputData]] */
  given bsonToInput: BsonDocumentDecoder[InputData] = bson =>
    InputData(
      findMoves = bson("findMoves").map(_.as[FindMovesData]),
      applyMove = bson("applyMove").map(_.as[ApplyMoveData]),
      promote = bson("promote").map(_.as[PromoteData])
    )

  /** A given [[BsonDocumentEncoder]] for [[InputData]] */
  given inputToBson: BsonDocumentEncoder[InputData] = i =>
    bson {
      i.findMoves.foreach { "findMoves" :: _ }
      i.applyMove.foreach { "applyMove" :: _ }
      i.promote.foreach { "promote" :: _ }
    }
