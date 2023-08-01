package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.SituationTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[SituationData]]. */
object SituationDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[SituationData]]. */
  given bsonToSituation: BsonDocumentDecoder[SituationData] = bson =>
    SituationData(
      situationType = bson.require("type").as[SituationTypeData],
      promotingPawnPosition = bson("promotingPawnPosition").map(_.as[PositionData])
    )

  /** A given [[BsonDocumentEncoder]] for [[SituationData]]. */
  given situationToBson: BsonDocumentEncoder[SituationData] = s =>
    bson {
      "type" :: s.situationType
      s.promotingPawnPosition.foreach { "promotingPawnPosition" :: _ }
    }
