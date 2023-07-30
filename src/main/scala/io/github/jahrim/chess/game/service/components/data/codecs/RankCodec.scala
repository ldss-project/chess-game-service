package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.Rank
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.BsonString
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
object RankCodec:

  given BsonDecoder[Rank] = bson => Rank.valueOf(bson.asString.getValue)

  given BsonEncoder[Rank] = rank => BsonString(s"_${rank.toString}")
