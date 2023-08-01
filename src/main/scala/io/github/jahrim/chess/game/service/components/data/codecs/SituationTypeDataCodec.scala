package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.SituationTypeData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object SituationDataCodec:

  given BsonDecoder[SituationTypeData] = bson => SituationTypeData.valueOf(bson.asString.getValue)

  given BsonEncoder[SituationTypeData] = s => BsonString(s.toString)
