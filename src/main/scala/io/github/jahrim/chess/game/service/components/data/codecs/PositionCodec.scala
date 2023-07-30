package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.PositionData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import org.bson.conversions.Bson
import FileCodec.given
import RankCodec.given
import io.github.chess.engine.model.board.File
import io.github.chess.engine.model.board.Rank
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** [[Bson]] codec for [[PositionData]] */
object PositionCodec:
  /** A given [[BsonDocumentDecoder]] for [[PositionData]] */
  given bsonToPosition: BsonDocumentDecoder[PositionData] = bson =>
    PositionData(
      file = bson.require("file").as[File],
      rank = bson.require("rank").as[Rank]
    )

  /** A given [[BsonDocumentEncoder]] for [[PositionData]] */
  given positionToBson: BsonDocumentEncoder[PositionData] = pos =>
    bson {
      "file" :: pos.file
      "rank" :: pos.rank
    }
