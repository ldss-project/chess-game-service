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

/** [[Bson]] codec for [[Throwable]]. */
object ChessGameServiceExceptionCodec:
  private val exceptionsPackage: String =
    classOf[ChessGameServiceException].getCanonicalName.stripSuffix(
      s".${classOf[ChessGameServiceException].getSimpleName}"
    )

  /** A given [[BsonDecoder]] for [[ChessGameServiceException]]. */
  given chessGameServiceExceptionDecoder[T <: ChessGameServiceException]: BsonDocumentDecoder[T] =
    bson =>
      getClass.getClassLoader
        .loadClass(s"$exceptionsPackage.${bson.require("type").as[String]}")
        .getConstructor(classOf[String])
        .newInstance(bson.require("message").as[String])
        .asInstanceOf[T]

  /** A given [[BsonEncoder]] for [[ChessGameServiceException]]. */
  given chessGameServiceExceptionEncoder[T <: ChessGameServiceException]: BsonDocumentEncoder[T] =
    exception =>
      bson {
        "type" :: exception.getClass.getSimpleName
        "message" :: exception.getMessage
      }
