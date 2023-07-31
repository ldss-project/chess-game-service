package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.{
  TimeConstraintData,
  TimeConstraintTypeData,
  GameConfigurationData
}
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameConfigurationDataCodec.given
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

class GameConfigurationDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for GameConfigurationData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "timeConstraint" :# {
          "type" :: "MoveLimit"
          "time" :# {
            "value" :: 2L
            "unit" :: "minutes"
          }
        }
        "gameId" :: "jnjgbng565fb"
        "isPrivate" :: true
      }
      val value: GameConfigurationData = document.as[GameConfigurationData]

      assert(
        value == GameConfigurationData(
          Option(TimeConstraintData(TimeConstraintTypeData.MoveLimit, Duration(2L, "minutes"))),
          Option("jnjgbng565fb"),
          true
        )
      )
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "timeConstraint" :# {
            "type" :: "MoveLimit"
            "time" :# {
              "value" :: 2L
              "unit" :: "minutes"
            }
          }
          "gameId" :: "jnjgbng565fb"
        }
        val value: GameConfigurationData = document.as[GameConfigurationData]
      }
    }
  }

  describe("A BsonEncoder for GameConfigurationData") {
    it("should encode properly a gameConfiguration") {
      val value: GameConfigurationData =
        GameConfigurationData(
          Option(TimeConstraintData(TimeConstraintTypeData.MoveLimit, Duration(2L, "minutes"))),
          Option("jnjgbng565fb"),
          true
        )
      val document: BsonDocument = value.asBson.asDocument
      assert(
        document("timeConstraint").map(_.as[TimeConstraintData]) ==
          Option(TimeConstraintData(TimeConstraintTypeData.MoveLimit, Duration(2L, "minutes")))
      )
      assert(document("gameId").map(_.as[String]) == Option("jnjgbng565fb"))
      assert(document.require("isPrivate").as[Boolean] == true)
    }
  }

  after {}
