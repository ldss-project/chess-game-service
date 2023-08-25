package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[LegacyTeamCodec]]. */
class LegacyTeamCodecTest extends BsonCodecTest(LegacyTeamCodecTest)

/** Companion object of [[LegacyTeamCodecTest]]. */
object LegacyTeamCodecTest extends BsonCodecTest.BsonCodecData[LegacyTeam]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyTeam](
      name = "White Team",
      sample = BsonString("WHITE"),
      expected = LegacyTeam.WHITE
    ),
    DecodeSample[LegacyTeam](
      name = "Black Team",
      sample = BsonString("BLACK"),
      expected = LegacyTeam.BLACK
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyTeam](
      name = "Unknown Team",
      sample = BsonString("UnknownLegacyTeam")
    )
  )
