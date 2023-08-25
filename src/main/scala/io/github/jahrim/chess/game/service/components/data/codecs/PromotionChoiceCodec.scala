package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.ports.model.game.state.PromotionChoice
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[PromotionChoice PromotionChoice]]. */
object PromotionChoiceCodec:
  /** A given [[BsonDecoder]] for [[PromotionChoice PromotionChoice]]. */
  given legacyPromotionChoiceDecoder: BsonDecoder[PromotionChoice] = bson =>
    PromotionChoice.valueOf(bson.asString.getValue)

  /** A given [[BsonEncoder]] for [[PromotionChoice PromotionChoice]]. */
  given legacyPromotionChoiceEncoder: BsonEncoder[PromotionChoice] = promotionChoice =>
    promotionChoice.toString.asBson
