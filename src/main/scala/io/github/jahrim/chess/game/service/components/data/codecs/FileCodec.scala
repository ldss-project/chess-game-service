package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.File
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.BsonString
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

object FileCodec:

  given BsonDecoder[File] = bson => File.valueOf(bson.asString.getValue)

  given BsonEncoder[File] = file => BsonString(file.toString.toUpperCase)
