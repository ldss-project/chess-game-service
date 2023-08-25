package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.Rank as LegacyRank
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyRankCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[LegacyRankCodec]]. */
class LegacyRankCodecTest extends BsonCodecTest(LegacyRankCodecTest)

/** Companion object of [[LegacyRankCodecTest]]. */
object LegacyRankCodecTest extends BsonCodecTest.BsonCodecData[LegacyRank]:
  override def decodeSamples: DecodeSampleIterable =
    LegacyRank.values.map(rank =>
      DecodeSample[LegacyRank](
        name = s"Rank $rank",
        sample = BsonString(rank.toString),
        expected = rank
      ),
    )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyRank](
      name = "Unknown Rank",
      sample = BsonString("UnknownLegacyRank")
    )
  )
