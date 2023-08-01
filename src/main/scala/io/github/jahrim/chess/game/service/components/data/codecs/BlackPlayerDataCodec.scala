package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[WhitePlayerData]]. */
object BlackPlayerDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[WhitePlayerData]]. */
  given bsonToWhitePlayer: BsonDocumentDecoder[WhitePlayerData] = bson =>
    WhitePlayerData(
      player = bson.require("player").as[PlayerData], 
      chessboard = bson.require("chessboard").as[ChessboardStatusData], 
      history = bson.require("history").as[Seq[MoveData]], 
      time = bson.require("time").as[Duration]
    )

  /** A given [[BsonDocumentEncoder]] for [[WhitePlayerData]]. */
  given whitePlayerToBson: BsonDocumentEncoder[WhitePlayerData] = w =>
    bson {
      "player" :: w.player
      "chessboard" :: w.chessboard
      "history" :: w.history
      "time" :: w.time
    }
