package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.Player as LegacyPlayer
import io.github.chess.engine.model.game.GameOverCause as LegacyGameOverCause
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyGameOverCauseCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPlayerCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameOver
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[GameOver]]. */
object GameOverCodec:
  /** A given [[BsonDecoder]] for [[GameOver]]. */
  given gameOverDecoder: BsonDocumentDecoder[GameOver] = bson =>
    GameOver(
      bson.require("cause").as[LegacyGameOverCause],
      bson.require("winner").as[Option[LegacyPlayer]]
    )

  /** A given [[BsonEncoder]] for [[GameOver]]. */
  given gameOverEncoder: BsonDocumentEncoder[GameOver] = gameOver =>
    bson {
      "cause" :: gameOver.cause
      "winner" :: gameOver.winner
    }
