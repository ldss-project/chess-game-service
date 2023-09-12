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

import io.github.jahrim.chess.game.service.components.data.codecs.JsonObjectCodec.*
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.JsonObject
import org.bson.BsonDocument

/** A [[MessageCodec]] for [[BsonDocument]]s. */
class BsonCodec extends MessageCodec[BsonDocument, BsonDocument]:
  override def encodeToWire(buffer: Buffer, s: BsonDocument): Unit =
    val jsonString = bsonToJson(s).encode()
    buffer.appendInt(jsonString.getBytes.length);
    buffer.appendString(jsonString);

  override def decodeFromWire(pos: Int, buffer: Buffer): BsonDocument =
    var cursor = pos

    val jsonStringLength = buffer.getInt(cursor)
    cursor = cursor + 4

    val jsonString = buffer.getString(cursor, pos + jsonStringLength)
    cursor = cursor + jsonStringLength

    jsonToBson(JsonObject(jsonString))

  override def transform(s: BsonDocument): BsonDocument = s
  override def name(): String = this.getClass.getCanonicalName
  override def systemCodecID(): Byte = -1

/** Companion object of [[BsonCodec]]. */
object BsonCodec:
  /** A singleton of [[BsonCodec]]. */
  val Singleton: BsonCodec = BsonCodec()
