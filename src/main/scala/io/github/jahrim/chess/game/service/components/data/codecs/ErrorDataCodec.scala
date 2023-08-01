package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.ErrorTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ErrorData]]. */
object ErrorDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[ErrorData]]. */
  given bsonToError: BsonDocumentDecoder[ErrorData] = bson =>
    ErrorData(
      typeError = bson.require("type").as[ErrorTypeData],
      message = bson.require("message").as[String]
    )

  /** A given [[BsonDocumentEncoder]] for [[ErrorData]]. */
  given errorToBson: BsonDocumentEncoder[ErrorData] = e =>
    bson {
      "type" :: e.typeError
      "message" :: e.message
    }
