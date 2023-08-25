package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.GameMode as LegacyGameMode
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyGameModeCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[LegacyGameModeCodec]]. */
class LegacyGameModeCodecTest extends BsonCodecTest(LegacyGameModeCodecTest)

/** Companion object of [[LegacyGameModeCodecTest]]. */
object LegacyGameModeCodecTest extends BsonCodecTest.BsonCodecData[LegacyGameMode]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyGameMode](
      name = "PvE",
      sample = BsonString("PVE"),
      expected = LegacyGameMode.PVE
    ),
    DecodeSample[LegacyGameMode](
      name = "PvP",
      sample = BsonString("PVP"),
      expected = LegacyGameMode.PVP
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyGameMode](
      name = "Unknown GameMode",
      sample = BsonString("UnknownGameMode")
    )
  )
