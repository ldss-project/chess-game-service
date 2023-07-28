package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.PieceTypeData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.BsonString

object PieceTypeDataCodec:

  given BsonDecoder[PieceTypeData] = bson =>
    PieceTypeData(bson.asString.getValue)

  given BsonEncoder[PieceTypeData] = pt =>
    BsonString(pt.toString)
