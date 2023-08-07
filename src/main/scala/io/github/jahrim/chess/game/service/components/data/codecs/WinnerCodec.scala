package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.Team
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object WinnerCodec:

  given BsonDecoder[Team] = bson => Team.valueOf(bson.asString.getValue)

  given BsonEncoder[Team] = t => BsonString(t.toString)
