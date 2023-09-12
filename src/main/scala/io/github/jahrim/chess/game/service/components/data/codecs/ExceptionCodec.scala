/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele, Kentpayeva Madina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
