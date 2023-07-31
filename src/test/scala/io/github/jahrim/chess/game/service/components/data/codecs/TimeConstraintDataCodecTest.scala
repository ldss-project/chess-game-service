package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import scala.concurrent.duration.Duration
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.TimeConstraintData
import io.github.jahrim.chess.game.service.components.data.TimeConstraintTypeData
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

class TimeConstraintDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for TimeConstraintData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "type" :: "MoveLimit"
        "time" :# {
          "value" :: 2L
          "unit" :: "minutes"
        }
      }
      val value: TimeConstraintData = document.as[TimeConstraintData]

      assert(value == TimeConstraintData(TimeConstraintTypeData.MoveLimit, Duration(2L, "minutes")))
    }
    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "type" :: "Limit"
          "time" :# {
            "value" :: 2L
            "unit" :: "minutes"
          }
        }
        val value: TimeConstraintData = document.as[TimeConstraintData]
      }
    }
  }

  describe("A BsonEncoder for TimeConstraintData") {
    it("should encode properly a timeConstraint") {
      val value: TimeConstraintData =
        TimeConstraintData(TimeConstraintTypeData.MoveLimit, Duration(2L, "minutes"))
      val document: BsonDocument = value.asBson.asDocument
      assert(
        document.require("type").as[TimeConstraintTypeData] == TimeConstraintTypeData.MoveLimit
      )
      assert(document.require("time").as[Duration] == Duration(2L, "minutes"))
    }
  }

  after {}
