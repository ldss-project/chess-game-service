package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.GameOverDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.WinnerCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CauseDataCodec.given
import io.github.jahrim.chess.game.service.components.data.GameOverData
import io.github.jahrim.chess.game.service.components.data.CauseData
import io.github.chess.engine.model.game.Team
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[GameOverData]]. */
object GameOverDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[GameOverData]]. */
  given bsonToGameOver: BsonDocumentDecoder[GameOverData] = bson =>
    GameOverData(
      cause = bson.require("cause").as[CauseData],
      winner = bson("winner").map(_.as[Team])
    )

  /** A given [[BsonDocumentEncoder]] for [[GameOverData]]. */
  given gameOverToBson: BsonDocumentEncoder[GameOverData] = g =>
    bson {
      "cause" :: g.cause
      g.winner.foreach { "winner" :: _ }
    }
