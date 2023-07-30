package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson
import io.github.jahrim.chess.game.service.components.data.GameConfigurationData
import io.github.jahrim.chess.game.service.components.data.TimeConstraintData

import TimeConstraintCodec.given
import GameConfigurationCodec.given

/** [[Bson]] codec for [[GameConfigurationData]]. */
object GameConfigurationCodec:
  /** A given [[BsonDocumentDecoder]] for [[GameConfigurationData]]. */
  given bsonToGameConfiguration: BsonDocumentDecoder[GameConfigurationData] = bson =>
    GameConfigurationData(
      timeConstraint = bson("gameConfiguration.timeConstraint").map(_.as[TimeConstraintData]),
      gameId = bson("gameConfiguration.gameId").map(_.as[String]),
      isPrivate = bson.require("gameConfiguration.isPrivate").as[Boolean]
    )

  /** A given [[BsonDocumentEncoder]] for [[GameConfigurationData]]. */
  given gameConfigurationToBson: BsonDocumentEncoder[GameConfigurationData] = gc =>
    bson {
      gc.timeConstraint.foreach { "timeConstraint" :: _ }
      gc.gameId.foreach { "gameId" :: _ }
      "private" :: gc.isPrivate
    }
