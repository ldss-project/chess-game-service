package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.TurnData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object TurnDataCodec:

  given BsonDecoder[TurnData] = bson => TurnData.valueOf(bson.asString.getValue)

  given BsonEncoder[TurnData] = t => BsonString(t.toString)
