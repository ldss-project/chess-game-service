package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{File, Rank}
import io.github.jahrim.chess.game.service.components.data.*

import scala.concurrent.duration.Duration
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ChessboardStatusDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerPerspectiveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ServerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.SituationDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.SituationTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerPerspectiveDataCodecTest.{
  playerPerspective,
  playerPerspectiveDocument,
  wrongPlayerPerspectiveDocument
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

class PlayerPerspectiveDataCodecTest extends CodecTest("PlayerPerspectiveData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = playerPerspectiveDocument
    val value: PlayerPerspectiveData = document.as[PlayerPerspectiveData]
    assert(value == playerPerspective)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongPlayerPerspectiveDocument
      val value: PlayerPerspectiveData = document.as[PlayerPerspectiveData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = playerPerspective.asBson.asDocument
    assert(document.require("player").as[PlayerData] == PlayerDataCodecTest.player)
    assert(
      document
        .require("chessboard")
        .as[ChessboardStatusData] == ChessboardStatusDataCodecTest.chessboardStatus
    )
    assert(document.require("history").as[Seq[MoveData]] == Seq(MoveDataCodecTest.move))
    assert(document.require("time").as[Duration] == DurationCodecTest.time)

object PlayerPerspectiveDataCodecTest:
  val playerPerspective: PlayerPerspectiveData =
    PlayerPerspectiveData(
      PlayerDataCodecTest.player,
      ChessboardStatusDataCodecTest.chessboardStatus,
      Seq(MoveDataCodecTest.move),
      DurationCodecTest.time
    )
  val playerPerspectiveDocument: BsonDocument =
    bson {
      "player" :: PlayerDataCodecTest.playerDocument
      "chessboard" :: ChessboardStatusDataCodecTest.chessboardStatusDocument
      "history" :* (MoveDataCodecTest.moveDocument)
      "time" :: DurationCodecTest.timeDocument
    }

  val wrongPlayerPerspectiveDocument: BsonDocument =
    bson {
      "InvalidField" :: PlayerDataCodecTest.playerDocument
      "chessboard" :: ChessboardStatusDataCodecTest.chessboardStatusDocument
      "history" :* (MoveDataCodecTest.moveDocument)
      "time" :: DurationCodecTest.timeDocument
    }
