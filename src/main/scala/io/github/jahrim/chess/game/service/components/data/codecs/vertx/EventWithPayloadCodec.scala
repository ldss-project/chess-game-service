package io.github.jahrim.chess.game.service.components.data.codecs.vertx

import io.github.jahrim.chess.game.service.components.data.codecs.EventWithPayloadCodec.{
  eventWithPayloadDecoder,
  eventWithPayloadEncoder
}
import io.github.jahrim.chess.game.service.components.events.Event
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec

import scala.reflect.{ClassTag, classTag}

/** A [[MessageCodec]] for [[Event.Payload EventWithPayload]]s. */
class EventWithPayloadCodec[
    E <: Event with Event.Payload[PayloadType]: ClassTag,
    PayloadType: ClassTag
](using
    BsonDecoder[PayloadType],
    BsonEncoder[PayloadType]
) extends MessageCodec[E, E]:
  override def encodeToWire(buffer: Buffer, s: E): Unit =
    BsonCodec.Singleton.encodeToWire(
      buffer,
      s.asBson(using eventWithPayloadEncoder[E, PayloadType]).asDocument
    )

  override def decodeFromWire(pos: Int, buffer: Buffer): E =
    BsonCodec.Singleton
      .decodeFromWire(pos, buffer)
      .as[E](using eventWithPayloadDecoder[E, PayloadType])

  override def transform(s: E): E = s
  override def name(): String = classTag[E].runtimeClass.getCanonicalName
  override def systemCodecID(): Byte = -1
