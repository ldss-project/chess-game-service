package io.github.jahrim.chess.game.service.components.data.codecs

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

/** [[Bson]] codec for [[Event]]. */
object EventCodec:
  private val eventsPackage: String =
    classOf[Event].getCanonicalName.stripSuffix(s".${classOf[Event].getSimpleName}")

  /** A given [[BsonDecoder]] for [[Event]]. */
  given eventDecoder[E <: Event]: BsonDocumentDecoder[E] = bson =>
    getClass.getClassLoader
      .loadClass(s"$eventsPackage.${bson.require("type").as[String]}")
      .getConstructor()
      .newInstance()
      .asInstanceOf[E]

  /** A given [[BsonEncoder]] for [[Event]]. */
  given eventEncoder[E <: Event]: BsonDocumentEncoder[E] = event =>
    bson {
      "type" :: event.name
    }
