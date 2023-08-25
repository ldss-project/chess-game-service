package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson
import org.bson.{BsonDocument, BsonNull}

/** [[Bson]] codec for [[Option]]. */
object OptionCodec:
  /** A given [[BsonDecoder]] for [[Option]]. */
  given optionDecoder[T](using decoder: BsonDecoder[T]): BsonDecoder[Option[T]] = _ match
    case none: BsonNull => None
    case some           => Some(decoder(some))

  /** A given [[BsonEncoder]] for [[Option]]. */
  given optionEncoder[T](using encoder: BsonEncoder[T]): BsonEncoder[Option[T]] = _ match
    case Some(some) => encoder(some)
    case None       => BsonNull.VALUE
