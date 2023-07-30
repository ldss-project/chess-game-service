package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.TimeConstraintTypeData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object TimeConstraintTypeDataCodec:

  given BsonDecoder[TimeConstraintTypeData] = bson =>
    TimeConstraintTypeData.valueOf(bson.asString.getValue)

  given BsonEncoder[TimeConstraintTypeData] = tt => BsonString(tt.toString)
