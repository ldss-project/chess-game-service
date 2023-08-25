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
