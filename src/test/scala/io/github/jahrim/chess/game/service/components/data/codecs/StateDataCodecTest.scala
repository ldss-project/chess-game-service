package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.GameOverDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.TurnDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerPerspectiveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.StateDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.SituationDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.SituationTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.StateDataCodecTest.{
  state,
  stateDocument,
  wrongStateDocument
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
import scala.Option

class StateDataCodecTest extends CodecTest("StateData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = stateDocument
    val value: StateData = document.as[StateData]
    assert(value == state)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongStateDocument
      val value: StateData = document.as[StateData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = state.asBson.asDocument
    assert(document.require("turn").as[TurnData] == TurnData.Black)
    assert(document.require("situation").as[SituationData] == SituationDataCodecTest.situation)
    assert(
      document
        .require("white")
        .as[PlayerPerspectiveData] == PlayerPerspectiveDataCodecTest.playerPerspective
    )
    assert(
      document
        .require("black")
        .as[PlayerPerspectiveData] == PlayerPerspectiveDataCodecTest.playerPerspective
    )
    assert(document.require("gameOver").as[GameOverData] == GameOverDataCodecTest.gameOver)

object StateDataCodecTest:
  val state: StateData =
    StateData(
      TurnData.Black,
      Option(SituationDataCodecTest.situation),
      PlayerPerspectiveDataCodecTest.playerPerspective,
      PlayerPerspectiveDataCodecTest.playerPerspective,
      Option(GameOverDataCodecTest.gameOver)
    )
  val stateDocument: BsonDocument =
    bson {
      "turn" :: TurnData.Black
      "situation" :: SituationDataCodecTest.situationDocument
      "white" :: PlayerPerspectiveDataCodecTest.playerPerspectiveDocument
      "black" :: PlayerPerspectiveDataCodecTest.playerPerspectiveDocument
      "gameOver" :: GameOverDataCodecTest.gameOverDocument
    }
  val wrongStateDocument: BsonDocument =
    bson {
      "InvalidField" :: TurnData.Black
      "situation" :: SituationDataCodecTest.situationDocument
      "white" :: PlayerPerspectiveDataCodecTest.playerPerspectiveDocument
      "black" :: PlayerPerspectiveDataCodecTest.playerPerspectiveDocument
      "gameOver" :: GameOverDataCodecTest.gameOverDocument
    }
