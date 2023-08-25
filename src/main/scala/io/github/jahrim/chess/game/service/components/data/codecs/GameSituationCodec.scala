package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.Position as LegacyPosition
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPositionCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameSituation
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[GameSituation]]. */
object GameSituationCodec:
  /** A given [[BsonDecoder]] for [[GameSituation]]. */
  given gameSituationDecoder: BsonDocumentDecoder[GameSituation] = bson =>
    bson.require("type").as[String] match
      case "None"      => GameSituation.None
      case "Check"     => GameSituation.Check
      case "Stale"     => GameSituation.Stale
      case "Checkmate" => GameSituation.Checkmate
      case "Promotion" =>
        GameSituation.Promotion(bson.require("promotingPawnPosition").as[LegacyPosition])

  /** A given [[BsonEncoder]] for [[GameSituation]]. */
  given gameSituationEncoder: BsonDocumentEncoder[GameSituation] = gameSituation =>
    bson {
      "type" :: gameSituation.productPrefix
      gameSituation match
        case GameSituation.Promotion(pawnPosition) =>
          "promotingPawnPosition" :: pawnPosition
        case _ =>
    }
