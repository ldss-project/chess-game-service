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
  private val eventsPackage: String = classOf[Event].getPackageName

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
