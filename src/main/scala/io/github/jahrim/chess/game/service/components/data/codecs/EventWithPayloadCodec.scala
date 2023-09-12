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

import io.github.jahrim.chess.game.service.components.data.codecs.EventCodec.given
import io.github.jahrim.chess.game.service.components.events.Event
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.reflect.{ClassTag, classTag}

/** [[Bson]] codec for [[Event.Payload]]. */
object EventWithPayloadCodec:
  private val eventsPackage: String =
    classOf[Event].getCanonicalName.stripSuffix(s".${classOf[Event].getSimpleName}")

  /** A given [[BsonDecoder]] for [[Event.Payload]]. */
  given eventWithPayloadDecoder[E <: Event with Event.Payload[PayloadType], PayloadType: ClassTag](
      using encoder: BsonDecoder[PayloadType]
  ): BsonDocumentDecoder[E] = bson =>
    getClass.getClassLoader
      .loadClass(s"$eventsPackage.${bson.require("type").as[String]}")
      .getConstructor(classTag[PayloadType].runtimeClass)
      .newInstance(bson.require("payload").as[PayloadType])
      .asInstanceOf[E]

  /** A given [[BsonEncoder]] for [[Event.Payload]]. */
  given eventWithPayloadEncoder[E <: Event with Event.Payload[PayloadType], PayloadType](using
      encoder: BsonEncoder[PayloadType]
  ): BsonDocumentEncoder[E] = event =>
    eventEncoder.encode(event).asDocument.update {
      "payload" :: event.payload
    }
