package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.ServerStateDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.ServerData
import io.github.jahrim.chess.game.service.components.data.ServerStateData
import io.github.jahrim.chess.game.service.components.data.ErrorData
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ServerData]] */
object ServerDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[ServerData]] */
  given bsonToServer: BsonDocumentDecoder[ServerData] = bson =>
    ServerData(
      state = bson.require("state").as[ServerStateData],
      error = bson("error").map(_.as[ErrorData])
    )

  /** A given [[BsonDocumentEncoder]] for [[ServerData]] */
  given serverToBson: BsonDocumentEncoder[ServerData] = s =>
    bson {
      "state" :: s.state
      s.error.foreach { "error" :: _ }
    }
