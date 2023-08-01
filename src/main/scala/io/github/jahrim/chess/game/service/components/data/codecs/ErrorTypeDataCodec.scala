package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.ErrorTypeData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object ErrorTypeDataCodec:

  given BsonDecoder[ErrorTypeData] = bson => ErrorTypeData.valueOf(bson.asString.getValue)

  given BsonEncoder[ErrorTypeData] = e => BsonString(e.toString)
