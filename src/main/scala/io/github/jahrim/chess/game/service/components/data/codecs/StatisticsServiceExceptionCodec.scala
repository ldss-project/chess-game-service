package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.proxies.statistics.exceptions.StatisticsServiceException
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[StatisticsServiceException]]. */
object StatisticsServiceExceptionCodec extends ExceptionCodec[StatisticsServiceException]:
  override protected val exceptionsPackage: String =
    classOf[StatisticsServiceException].getCanonicalName.stripSuffix(
      s".${classOf[StatisticsServiceException].getSimpleName}"
    )

  /** A [[BsonDecoder]] for [[StatisticsServiceException]]. */
  given statisticsServiceExceptionDecoder[T <: StatisticsServiceException]: BsonDocumentDecoder[T] =
    decodeException

  /** A given [[BsonEncoder]] for [[StatisticsServiceException]]. */
  given statisticsServiceExceptionEncoder[T <: StatisticsServiceException]: BsonDocumentEncoder[T] =
    encodeException
