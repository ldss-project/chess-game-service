package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.StateDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given

import io.github.jahrim.chess.game.service.components.data.codecs.GameOverDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[GameData]]. */
object GameDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[GameData]]. */
  given bsonToGame: BsonDocumentDecoder[GameData] = bson =>
    GameData(
      server = bson.require("server").as[ServerData],
      state = bson.require("state").as[StateData]
    )

  /** A given [[BsonDocumentEncoder]] for [[GameData]]. */
  given gameToBson: BsonDocumentEncoder[GameData] = g =>
    bson {
      "server" :: g.server
      "state" :: g.state
    }
