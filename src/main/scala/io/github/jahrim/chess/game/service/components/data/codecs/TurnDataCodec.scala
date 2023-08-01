package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.ServerStateData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object TurnDataCodec:

  given BsonDecoder[ServerStateData] = bson => ServerStateData.valueOf(bson.asString.getValue)

  given BsonEncoder[ServerStateData] = st => BsonString(st.toString)
