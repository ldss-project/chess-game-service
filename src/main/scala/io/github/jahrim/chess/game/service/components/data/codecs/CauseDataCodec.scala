package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.CauseData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object CauseDataCodec:

  given BsonDecoder[CauseData] = bson => CauseData.valueOf(bson.asString.getValue)

  given BsonEncoder[CauseData] = c => BsonString(c.toString)
