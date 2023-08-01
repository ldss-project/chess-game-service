package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.File
import io.github.chess.engine.model.board.Rank
import io.github.jahrim.chess.game.service.components.data.codecs.SituationDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.SituationTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
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

class SituationDataCodecTest extends AbstractTest:

  before {}

  describe("A BsonDecoder for SituationData") {
    it("should decode properly a correct bson") {
      val document: BsonDocument = bson {
        "type" :: "Stale"
        "promotingPawnPosition" :# {
          "file" :: "B"
          "rank" :: "_5"
        }
      }
      val value: SituationData = document.as[SituationData]

      assert(
        value == SituationData(
          SituationTypeData.Stale,
          Option(PositionData(File.B, Rank._5))
        )
      )

    }

    it("should throw an error when decoding a wrong bson") {
      assertThrows[Exception] {
        val document: BsonDocument = bson {
          "type" :: "S"
          "promotingPawnPosition" :# {
            "file" :: "B"
            "rank" :: "_11"
          }
        }
        val value: SituationData = document.as[SituationData]
      }
    }
  }

  describe("A BsonEncoder for SituationData") {
    it("should encode properly a situation") {
      val value: SituationData = SituationData(
        SituationTypeData.Stale,
        Option(PositionData(File.B, Rank._5))
      )
      val document: BsonDocument = value.asBson.asDocument
      assert(document.require("type").as[SituationTypeData] == SituationTypeData.Stale)
      assert(
        document("promotingPawnPosition").map(_.as[PositionData]) == Option(
          PositionData(File.B, Rank._5)
        )
      )

    }
  }

  after {}
