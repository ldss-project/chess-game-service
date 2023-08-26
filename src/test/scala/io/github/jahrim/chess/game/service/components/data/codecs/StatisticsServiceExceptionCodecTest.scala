package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.StatisticsServiceExceptionCodec.given
import io.github.jahrim.chess.game.service.components.proxies.statistics.exceptions.{
  StatisticsServiceException,
  MalformedInputException
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[StatisticsServiceExceptionCodec]]. */
class StatisticsServiceExceptionCodecTest extends BsonCodecTest(StatisticsServiceExceptionCodecTest)

/** Companion object of [[StatisticsServiceExceptionCodecTest]]. */
object StatisticsServiceExceptionCodecTest
    extends BsonCodecTest.BsonCodecData[StatisticsServiceException]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[StatisticsServiceException](
      name = "StatisticsServiceException",
      sample = bson { "type" :: "StatisticsServiceException"; "message" :: "ErrorMessage" },
      expected = StatisticsServiceException("ErrorMessage")
    ),
    DecodeSample[StatisticsServiceException](
      name = "Specific StatisticsServiceException",
      sample = bson { "type" :: "MalformedInputException"; "message" :: "ErrorMessage" },
      expected = MalformedInputException("ErrorMessage")
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[StatisticsServiceException](
      name = "Unknown exception",
      sample = bson { "type" :: "UnknownException"; "message" :: "ErrorMessage" }
    )
  )
