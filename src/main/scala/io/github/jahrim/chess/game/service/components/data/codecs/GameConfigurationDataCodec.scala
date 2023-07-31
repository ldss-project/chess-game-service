package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.GameConfigurationData
import io.github.jahrim.chess.game.service.components.data.TimeConstraintData

import TimeConstraintDataCodec.given
import GameConfigurationDataCodec.given

/** [[Bson]] codec for [[GameConfigurationData]]. */
object GameConfigurationDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[GameConfigurationData]]. */
  given bsonToGameConfiguration: BsonDocumentDecoder[GameConfigurationData] = bson =>
    GameConfigurationData(
      timeConstraint = bson("timeConstraint").map(_.as[TimeConstraintData]),
      gameId = bson("gameId").map(_.as[String]),
      isPrivate = bson.require("isPrivate").as[Boolean]
    )

  /** A given [[BsonDocumentEncoder]] for [[GameConfigurationData]]. */
  given gameConfigurationToBson: BsonDocumentEncoder[GameConfigurationData] = gc =>
    bson {
      gc.timeConstraint.foreach { "timeConstraint" :: _ }
      gc.gameId.foreach { "gameId" :: _ }
      "isPrivate" :: gc.isPrivate
    }
