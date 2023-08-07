package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.GameDataCodecTest.{
  game,
  gameDocument,
  wrongGameDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameDataCodec.given

import io.github.jahrim.chess.game.service.components.data.codecs.StateDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.StateDataCodecTest.{
  state,
  stateDocument,
  wrongStateDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.TurnDataCodec.given
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

import scala.Option

class GameDataCodecTest extends CodecTest("GameData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = gameDocument
    val value: GameData = document.as[GameData]
    assert(value == game)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongGameDocument
      val value: GameData = document.as[GameData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = game.asBson.asDocument
    assert(document.require("server").as[ServerData] == ServerDataCodecTest.server)
    assert(document.require("state").as[StateData] == StateDataCodecTest.state)

object GameDataCodecTest:
  val game: GameData =
    GameData(
      ServerDataCodecTest.server,
      StateDataCodecTest.state
    )
  val gameDocument: BsonDocument =
    bson {
      "server" :: ServerDataCodecTest.serverDocument
      "state" :: StateDataCodecTest.stateDocument
    }
  val wrongGameDocument: BsonDocument =
    bson {
      "InvalidField" :: ServerDataCodecTest.serverDocument
      "state" :: StateDataCodecTest.stateDocument
    }
