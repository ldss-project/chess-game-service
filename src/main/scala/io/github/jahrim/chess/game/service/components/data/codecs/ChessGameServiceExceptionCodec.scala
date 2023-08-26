package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.exceptions.ChessGameServiceException
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[ChessGameServiceException]]. */
object ChessGameServiceExceptionCodec extends ExceptionCodec[ChessGameServiceException]:
  override protected val exceptionsPackage: String =
    classOf[ChessGameServiceException].getCanonicalName.stripSuffix(
      s".${classOf[ChessGameServiceException].getSimpleName}"
    )

  /** A given [[BsonDecoder]] for [[ChessGameServiceException]]. */
  given chessGameServiceExceptionDecoder[T <: ChessGameServiceException]: BsonDocumentDecoder[T] =
    decodeException

  /** A given [[BsonEncoder]] for [[ChessGameServiceException]]. */
  given chessGameServiceExceptionEncoder[T <: ChessGameServiceException]: BsonDocumentEncoder[T] =
    encodeException
