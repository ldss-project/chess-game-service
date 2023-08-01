package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.PieceTypeData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

object ErrorTypeDataCodec:

  given BsonDecoder[PieceTypeData] = bson => PieceTypeData.valueOf(bson.asString.getValue)

  given BsonEncoder[PieceTypeData] = pt => BsonString(pt.toString)
