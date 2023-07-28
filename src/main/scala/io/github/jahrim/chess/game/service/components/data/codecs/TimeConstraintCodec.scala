package io.github.jahrim.chess.game.service.components.data.codecs

import DurationCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson
import io.github.chess.engine.model.configuration.TimeConstraint

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[TimeConstraint]]. */
object TimeConstraintCodec:
  /** A given [[BsonDocumentDecoder]] for [[TimeConstraint]]. */
  given bsonToTimeConstraint: BsonDocumentDecoder[TimeConstraint] = bson =>
    val constraint = bson.require("type").as[String] match
      case "MoveLimit" => TimeConstraint.MoveLimit
      case "PlayerLimit" => TimeConstraint.PlayerLimit
      case _ => TimeConstraint.NoLimit
    constraint.minutes = bson.require("time").as[Duration].toMinutes.asInstanceOf[Int] //TODO make Long
    constraint

  /** A given [[BsonDocumentEncoder]] for [[TimeConstraint]]. */
  given timeConstraintToBson: BsonDocumentEncoder[TimeConstraint] = ts =>
    bson {
      "type" :: ts match
        case _: TimeConstraint.MoveLimit => "MoveLimit"
        case _: TimeConstraint.PlayerLimit => "PlayerLimit"
        case _ => "NoLimit"
      "time" :: Duration(ts.minutes, TimeUnit.MINUTES)
    }
