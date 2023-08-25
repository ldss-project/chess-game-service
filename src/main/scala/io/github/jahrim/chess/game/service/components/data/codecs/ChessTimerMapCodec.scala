package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameState.ChessTimerMap
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[ChessTimerMap ChessTimerMap]]. */
object ChessTimerMapCodec:
  /** A given [[BsonDecoder]] for [[ChessTimerMap ChessTimerMap]]. */
  given chessTimerMapDecoder: BsonDocumentDecoder[ChessTimerMap] = bson =>
    Map(LegacyTeam.WHITE -> bson("white"), LegacyTeam.BLACK -> bson("black"))
      .collect {
        case (team, Some(bsonTimer)) if bsonTimer.isDocument => (team, bsonTimer.as[Duration])
      }

  /** A given [[BsonEncoder]] for [[ChessTimerMap ChessTimerMap]]. */
  given chessTimerMapEncoder: BsonDocumentEncoder[ChessTimerMap] = chessTimerMap =>
    bson {
      "white" :: chessTimerMap.get(LegacyTeam.WHITE)
      "black" :: chessTimerMap.get(LegacyTeam.BLACK)
    }
