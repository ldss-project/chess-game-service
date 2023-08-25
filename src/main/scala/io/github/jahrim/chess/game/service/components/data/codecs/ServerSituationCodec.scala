package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ServerSituation]]. */
object ServerSituationCodec:
  /** A given [[BsonDecoder]] for [[ServerSituation]]. */
  given serverSituationDecoder: BsonDecoder[ServerSituation] = bson =>
    ServerSituation.valueOf(bson.asString.getValue)

  /** A given [[BsonEncoder]] for [[ServerSituation]]. */
  given serverSituationEncoder: BsonEncoder[ServerSituation] = serverSituation =>
    serverSituation.toString.asBson
