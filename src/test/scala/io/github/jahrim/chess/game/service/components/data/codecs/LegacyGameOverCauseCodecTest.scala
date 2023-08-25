package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.GameOverCause as LegacyGameOverCause
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyGameOverCauseCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[LegacyGameOverCauseCodec]]. */
class LegacyGameOverCauseCodecTest extends BsonCodecTest(LegacyGameOverCauseCodecTest)

/** Companion object of [[LegacyGameOverCauseCodecTest]]. */
object LegacyGameOverCauseCodecTest extends BsonCodecTest.BsonCodecData[LegacyGameOverCause]:
  override def decodeSamples: DecodeSampleIterable =
    LegacyGameOverCause.values.map(gameOverCause =>
      DecodeSample[LegacyGameOverCause](
        name = gameOverCause.productPrefix,
        sample = BsonString(gameOverCause.productPrefix),
        expected = gameOverCause
      )
    )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyGameOverCause](
      name = "Unknown GameOverCause",
      sample = BsonString("UnknownGameOverCause")
    )
  )
