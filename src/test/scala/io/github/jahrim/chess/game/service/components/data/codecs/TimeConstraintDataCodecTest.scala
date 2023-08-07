package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}

import scala.concurrent.duration.Duration
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.TimeConstraintData
import io.github.jahrim.chess.game.service.components.data.TimeConstraintTypeData
import io.github.jahrim.chess.game.service.components.data.codecs.TimeConstraintDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.TimeConstraintDataCodecTest.{
  timeConstraint,
  timeConstraintDocument,
  wrongTimeConstraintDocument
}
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

class TimeConstraintDataCodecTest extends CodecTest("TimeConstraintData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = timeConstraintDocument
    val value: TimeConstraintData = document.as[TimeConstraintData]
    assert(value == timeConstraint)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongTimeConstraintDocument
      val value: TimeConstraintData = document.as[TimeConstraintData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = timeConstraint.asBson.asDocument
    assert(document.require("type").as[TimeConstraintTypeData] == TimeConstraintTypeData.MoveLimit)
    assert(document.require("time").as[Duration] == DurationCodecTest.time)

object TimeConstraintDataCodecTest:
  val timeConstraint: TimeConstraintData =
    TimeConstraintData(TimeConstraintTypeData.MoveLimit, DurationCodecTest.time)
  val timeConstraintDocument: BsonDocument =
    bson {
      "type" :: "MoveLimit"
      "time" :: DurationCodecTest.timeDocument
    }
  val wrongTimeConstraintDocument: BsonDocument =
    bson {
      "type" :: "InvalidType"
      "time" :: DurationCodecTest.timeDocument
    }
