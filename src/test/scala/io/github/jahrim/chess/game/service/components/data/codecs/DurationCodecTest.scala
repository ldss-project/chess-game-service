package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodecTest.{
  time,
  timeDocument,
  wrongTimeDocument
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

import scala.concurrent.duration.Duration
import test.AbstractTest

class DurationCodecTest extends CodecTest("Duration"):
  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = timeDocument
    val value: Duration = document.as[Duration]

    assert(value == time)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongTimeDocument
      val value: Duration = document.as[Duration]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = time.asBson.asDocument
    assert(document.require("value").as[Long] == 2L)
    assert(document.require("unit").as[String] == "MINUTES")

object DurationCodecTest:
  val time: Duration = Duration(2L, "minutes")
  val timeDocument: BsonDocument = bson {
    "value" :: 2L
    "unit" :: "minutes"
  }
  val wrongTimeDocument: BsonDocument = bson {
    "value" :: 2L
    "unit" :: "InvalidUnit"
  }
