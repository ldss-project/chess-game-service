package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.MoveTypeData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.BsonString
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

object MoveTypeCodec:

  given BsonDecoder[MoveTypeData] = bson => MoveTypeData.valueOf(bson.asString.getValue)

  given BsonEncoder[MoveTypeData] = tp => BsonString(tp.toString)
