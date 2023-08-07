package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.GameOverData
import io.github.jahrim.chess.game.service.components.data.CauseData
import io.github.chess.engine.model.game.Team
import io.github.jahrim.chess.game.service.components.data.codecs.GameOverDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.CauseDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.GameOverDataCodecTest.{
  gameOver,
  gameOverDocument,
  wrongGameOverDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.WinnerCodec.given
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

class GameOverDataCodecTest extends CodecTest("GameOverData"):
  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = gameOverDocument
    val value: GameOverData = document.as[GameOverData]
    assert(value == gameOver)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] { wrongGameOverDocument.as[GameOverData] }

  override def encodeTest(): Unit =
    val document: BsonDocument = gameOver.asBson.asDocument
    assert(document.require("cause").as[CauseData] == CauseData.Stale)
    assert(document.require("winner").as[Team] == Team.BLACK)

object GameOverDataCodecTest:
  val gameOver: GameOverData = GameOverData(CauseData.Stale, Option(Team.BLACK))
  val gameOverDocument: BsonDocument =
    bson {
      "cause" :: "Stale"
      "winner" :: "BLACK"
    }
  val wrongGameOverDocument: BsonDocument =
    bson {
      "Invalid" :: "Stale"
      "winner" :: "BLACK"
    }
