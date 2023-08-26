package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/**
 * [[Bson]] codec for the specified type of exceptions.
 * @tparam E the specified of exceptions.
 */
trait ExceptionCodec[E <: Exception]:
  /** The type of exceptions (de)serialized by this [[ExceptionCodec]]. */
  protected type ExceptionType = E

  /** The package where the exceptions of type [[ExceptionType ExceptionType]] are stored. */
  protected def exceptionsPackage: String

  /** A [[BsonDecoder]] for [[ExceptionType ExceptionType]]. */
  protected def decodeException[T <: ExceptionType]: BsonDocumentDecoder[T] = bson =>
    getClass.getClassLoader
      .loadClass(s"$exceptionsPackage.${bson.require("type").as[String]}")
      .getConstructor(classOf[String])
      .newInstance(bson.require("message").as[String])
      .asInstanceOf[T]

  /** A [[BsonEncoder]] for [[ExceptionType ExceptionType]]. */
  protected def encodeException[T <: ExceptionType]: BsonDocumentEncoder[T] = exception =>
    bson {
      "type" :: exception.getClass.getSimpleName
      "message" :: exception.getMessage
    }
