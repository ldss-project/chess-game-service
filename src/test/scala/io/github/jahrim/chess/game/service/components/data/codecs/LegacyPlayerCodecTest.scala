package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  Player as LegacyPlayer,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPlayerCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[LegacyPlayerCodec]]. */
class LegacyPlayerCodecTest extends BsonCodecTest(LegacyPlayerCodecTest)

/** Companion object of [[LegacyPlayerCodecTest]]. */
object LegacyPlayerCodecTest extends BsonCodecTest.BsonCodecData[LegacyPlayer]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[LegacyPlayer](
      name = "White player",
      sample = bson { "name" :: "PlayerName"; "team" :: LegacyTeam.WHITE },
      expected = LegacyWhitePlayer("PlayerName")
    ),
    DecodeSample[LegacyPlayer](
      name = "Black player",
      sample = bson { "name" :: "PlayerName"; "team" :: LegacyTeam.BLACK },
      expected = LegacyBlackPlayer("PlayerName")
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyPlayer](
      name = "Player without name",
      sample = bson { "team" :: LegacyTeam.BLACK }
    ),
    WrongDecodeSample[LegacyPlayer](
      name = "Player without team",
      sample = bson { "name" :: "PlayerName" }
    )
  )
