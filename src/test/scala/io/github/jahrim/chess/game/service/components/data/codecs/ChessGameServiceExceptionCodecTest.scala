package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.ChessGameServiceExceptionCodec.given
import io.github.jahrim.chess.game.service.components.exceptions.{
  ChessGameServiceException,
  GameNotRunningException
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[ChessGameServiceExceptionCodec]]. */
class ChessGameServiceExceptionCodecTest extends BsonCodecTest(ChessGameServiceExceptionCodecTest)

/** Companion object of [[ChessGameServiceExceptionCodecTest]]. */
object ChessGameServiceExceptionCodecTest
    extends BsonCodecTest.BsonCodecData[ChessGameServiceException]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[ChessGameServiceException](
      name = "ChessGameServiceException",
      sample = bson { "type" :: "ChessGameServiceException"; "message" :: "ErrorMessage" },
      expected = ChessGameServiceException("ErrorMessage")
    ),
    DecodeSample[ChessGameServiceException](
      name = "Specific ChessGameServiceException",
      sample = bson {
        "type" :: "GameNotRunningException";
        "message" :: "Game 'ID' rejected the request: not running."
      },
      expected = GameNotRunningException("ID")
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[ChessGameServiceException](
      name = "Unknown exception",
      sample = bson { "type" :: "UnknownException"; "message" :: "ErrorMessage" }
    )
  )
