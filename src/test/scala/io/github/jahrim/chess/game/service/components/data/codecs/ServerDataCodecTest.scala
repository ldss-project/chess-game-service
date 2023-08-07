package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ErrorDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerStateDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodecTest.{
  server,
  serverDocument,
  wrongServerDocument
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

class ServerDataCodecTest extends CodecTest("ServerData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = serverDocument
    val value: ServerData = document.as[ServerData]
    assert(value == server)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongServerDocument
      val value: ServerData = document.as[ServerData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = server.asBson.asDocument
    assert(document.require("state").as[ServerStateData] == ServerStateData.Running)
    assert(document.require("error").as[ErrorData] == ErrorDataCodecTest.error)

object ServerDataCodecTest:
  val server: ServerData = ServerData(
    ServerStateData.Running,
    Option(ErrorDataCodecTest.error)
  )
  val serverDocument: BsonDocument =
    bson {
      "state" :: "Running"
      "error" :: ErrorDataCodecTest.errorDocument
    }
  val wrongServerDocument: BsonDocument =
    bson {
      "state" :: "InvalidState"
      "error" :: ErrorDataCodecTest.errorDocument
    }
