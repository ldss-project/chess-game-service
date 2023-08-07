package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ChessboardStatusDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerPerspectiveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson
import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[PlayerPerspectiveData]]. */
object PlayerPerspectiveDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[PlayerPerspectiveData]]. */
  given bsonToWhitePlayer: BsonDocumentDecoder[PlayerPerspectiveData] = bson =>
    PlayerPerspectiveData(
      player = bson.require("player").as[PlayerData],
      chessboard = bson.require("chessboard").as[ChessboardStatusData],
      history = bson.require("history").as[Seq[MoveData]],
      time = bson.require("time").as[Duration]
    )

  /** A given [[BsonDocumentEncoder]] for [[PlayerPerspectiveData]]. */
  given whitePlayerToBson: BsonDocumentEncoder[PlayerPerspectiveData] = w =>
    bson {
      "player" :: w.player
      "chessboard" :: w.chessboard
      "history" :: w.history
      "time" :: w.time
    }
