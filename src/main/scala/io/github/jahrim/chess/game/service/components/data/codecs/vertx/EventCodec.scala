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
