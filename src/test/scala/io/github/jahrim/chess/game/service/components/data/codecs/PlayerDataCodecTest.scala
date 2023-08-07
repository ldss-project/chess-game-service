package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.PlayerData
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerDataCodecTest.{
  player,
  playerDocument,
  wrongPlayerDocument
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

class PlayerDataCodecTest extends CodecTest("PlayerData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = playerDocument
    val value: PlayerData = document.as[PlayerData]
    assert(value == player)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongPlayerDocument
      val value: PlayerData = document.as[PlayerData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = player.asBson.asDocument
    assert(document.require("username").as[String] == "playerName")

object PlayerDataCodecTest:
  val player: PlayerData = PlayerData("playerName")
  val playerDocument: BsonDocument = bson {
    "username" :: "playerName"
  }
  val wrongPlayerDocument: BsonDocument = bson {
    "InvalidField" :: "playerName"
  }
