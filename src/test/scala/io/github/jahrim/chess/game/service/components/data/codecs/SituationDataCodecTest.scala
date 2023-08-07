package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.codecs.SituationDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.SituationTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.SituationDataCodecTest.{
  situation,
  situationDocument,
  wrongSituationDocument
}
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

class SituationDataCodecTest extends CodecTest("SituationData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = situationDocument
    val value: SituationData = document.as[SituationData]
    assert(value == situation)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongSituationDocument
      val value: SituationData = document.as[SituationData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = situation.asBson.asDocument
    assert(document.require("type").as[SituationTypeData] == SituationTypeData.Stale)
    assert(
      document.require("promotingPawnPosition").as[PositionData] == PositionDataCodecTest.position
    )

object SituationDataCodecTest:
  val situation: SituationData =
    SituationData(
      SituationTypeData.Stale,
      Option(PositionDataCodecTest.position)
    )
  val situationDocument: BsonDocument =
    bson {
      "type" :: "Stale"
      "promotingPawnPosition" :: PositionDataCodecTest.positionDocument
    }
  val wrongSituationDocument: BsonDocument =
    bson {
      "type" :: "InvalidType"
      "promotingPawnPosition" :: PositionDataCodecTest.positionDocument
    }
