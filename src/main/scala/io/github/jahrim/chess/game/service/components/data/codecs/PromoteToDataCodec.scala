package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.PromoteToData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object PromoteToDataCodec:

  given BsonDecoder[PromoteToData] = bson => PromoteToData.valueOf(bson.asString.getValue)

  given BsonEncoder[PromoteToData] = t => BsonString(t.toString)
