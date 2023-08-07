package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  GameConfigurationData,
  TimeConstraintData,
  TimeConstraintTypeData
}
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameConfigurationDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameConfigurationDataCodecTest.{
  gameConfiguration,
  gameConfigurationDocument,
  wrongGameConfigurationDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.TimeConstraintDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.TimeConstraintTypeDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonDocument
import org.bson.conversions.Bson
import test.AbstractTest

import scala.concurrent.duration.Duration

class GameConfigurationDataCodecTest extends CodecTest("GameConfigurationData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = gameConfigurationDocument
    val value: GameConfigurationData = document.as[GameConfigurationData]
    assert(value == gameConfiguration)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongGameConfigurationDocument
      val value: GameConfigurationData = document.as[GameConfigurationData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = gameConfiguration.asBson.asDocument
    assert(
      document
        .require("timeConstraint")
        .as[TimeConstraintData] == TimeConstraintDataCodecTest.timeConstraint
    )
    assert(document.require("gameId").as[String] == "gameId")
    assert(document.require("isPrivate").as[Boolean] == true)

object GameConfigurationDataCodecTest:
  val gameConfiguration: GameConfigurationData =
    GameConfigurationData(
      Option(TimeConstraintDataCodecTest.timeConstraint),
      Option("gameId"),
      true
    )
  val gameConfigurationDocument: BsonDocument =
    bson {
      "timeConstraint" :: TimeConstraintDataCodecTest.timeConstraintDocument
      "gameId" :: "gameId"
      "isPrivate" :: true
    }
  val wrongGameConfigurationDocument: BsonDocument =
    bson {
      "timeConstraint" :: TimeConstraintDataCodecTest.timeConstraintDocument
      "gameId" :: "gameId"
      "InvalidField" :: true
    }
