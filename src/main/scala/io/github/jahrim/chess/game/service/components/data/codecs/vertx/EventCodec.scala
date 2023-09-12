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
package io.github.jahrim.chess.game.service.components.data.codecs.vertx

import io.github.jahrim.chess.game.service.components.events.Event
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec

import scala.reflect.{ClassTag, classTag}

/** A [[MessageCodec]] for [[Event]]s. */
class EventCodec[E <: Event: ClassTag](using BsonDecoder[E], BsonEncoder[E])
    extends MessageCodec[E, E]:
  override def encodeToWire(buffer: Buffer, s: E): Unit =
    BsonCodec.Singleton.encodeToWire(buffer, s.asBson.asDocument)

  override def decodeFromWire(pos: Int, buffer: Buffer): E =
    BsonCodec.Singleton.decodeFromWire(pos, buffer).as[E]

  override def transform(s: E): E = s
  override def name(): String = classTag[E].runtimeClass.getCanonicalName
  override def systemCodecID(): Byte = -1
