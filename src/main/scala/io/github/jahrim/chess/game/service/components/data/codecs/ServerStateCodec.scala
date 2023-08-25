package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.ChessGameServiceExceptionCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameStateCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerSituationCodec.given
import io.github.jahrim.chess.game.service.components.exceptions.ChessGameServiceException
import io.github.jahrim.chess.game.service.components.ports.model.game.state.{
  GameState,
  ServerSituation,
  ServerState,
  TimeConstraint
}
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ServerState ServerState]]. */
object ServerStateCodec:
  /** A given [[BsonDecoder]] for [[ServerState ServerState]]. */
  given serverStateDecoder: BsonDocumentDecoder[ServerState] = bson =>
    ServerState(
      serverSituation = bson.require("situation").as[ServerSituation],
      serverErrorOption = bson.require("error").as[Option[ChessGameServiceException]],
      // subscriptions consumers are not deserialized
      gameState = bson.require("gameState").as[GameState]
    )

  /** A given [[BsonEncoder]] for [[ServerState ServerState]]. */
  given serverStateEncoder: BsonDocumentEncoder[ServerState] = serverState =>
    bson {
      "situation" :: serverState.serverSituation
      "subscriptions" :: serverState.subscriptions.keys.toSeq
      "error" :: serverState.serverErrorOption
      "gameState" :: serverState.gameState
    }
