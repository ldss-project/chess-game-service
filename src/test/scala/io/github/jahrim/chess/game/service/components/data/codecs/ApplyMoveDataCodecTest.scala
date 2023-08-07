package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.CastlingDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.FileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodecTest.{
  move,
  moveDocument,
  wrongMoveDocument
}
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PieceTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.RankCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.ApplyMoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.ApplyMoveDataCodecTest.{
  applyMove,
  applyMoveDocument,
  wrongApplyMoveDocument
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

class ApplyMoveDataCodecTest extends CodecTest("ApplyMoveData"):

  override def decodeCorrectBsonTest(): Unit =
    val document: BsonDocument = applyMoveDocument
    val value: ApplyMoveData = document.as[ApplyMoveData]
    assert(value == applyMove)

  override def decodeWrongBsonTest(): Unit =
    assertThrows[Exception] {
      val document: BsonDocument = wrongApplyMoveDocument
      val value: ApplyMoveData = document.as[ApplyMoveData]
    }

  override def encodeTest(): Unit =
    val document: BsonDocument = applyMove.asBson.asDocument
    assert(document.require("move").as[MoveData] == MoveDataCodecTest.move)

object ApplyMoveDataCodecTest:
  val applyMove: ApplyMoveData = ApplyMoveData(
    MoveDataCodecTest.move
  )
  val applyMoveDocument: BsonDocument = bson {
    "move" :: MoveDataCodecTest.moveDocument
  }
  val wrongApplyMoveDocument: BsonDocument = bson {
    "InvalidField" :: MoveDataCodecTest.moveDocument
  }
