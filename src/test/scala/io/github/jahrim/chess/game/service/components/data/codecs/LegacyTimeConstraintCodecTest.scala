package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.TimeConstraint as LegacyTimeConstraint
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTimeConstraintCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.TimeConstraint
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.{BsonNull, BsonString}

/** A [[BsonCodecTest]] for [[LegacyTimeConstraintCodec]]. */
class LegacyTimeConstraintCodecTest extends BsonCodecTest(LegacyTimeConstraintCodecTest)

/** Companion object of [[LegacyTimeConstraintCodecTest]]. */
object LegacyTimeConstraintCodecTest extends BsonCodecTest.BsonCodecData[LegacyTimeConstraint]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyTimeConstraint](
      name = "NoLimit",
      sample = bson { "type" :: "NoLimit"; "time" :: BsonNull.VALUE },
      expected = LegacyTimeConstraint.NoLimit
    ),
    DecodeSample[LegacyTimeConstraint](
      name = "PlayerLimit",
      sample = bson { "type" :: "PlayerLimit"; "time" :: DurationCodecTest.bsonSample },
      expected =
        TimeConstraint.PlayerLimit.withDuration(DurationCodecTest.dataSample.toMinutes.toInt).legacy
    ),
    DecodeSample[LegacyTimeConstraint](
      name = "MoveLimit",
      sample = bson { "type" :: "MoveLimit"; "time" :: DurationCodecTest.bsonSample },
      expected =
        TimeConstraint.MoveLimit.withDuration(DurationCodecTest.dataSample.toMinutes.toInt).legacy
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyTimeConstraint](
      name = "Unknown TimeConstraint",
      sample = bson { "type" :: "UnknownLegacyTimeConstraint" }
    )
  )
