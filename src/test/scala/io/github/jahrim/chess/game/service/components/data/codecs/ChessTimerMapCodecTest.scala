package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.ChessTimerMapCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameState.ChessTimerMap
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonNull

/** A [[BsonCodecTest]] for [[ChessTimerMapCodec]]. */
class ChessTimerMapCodecTest extends BsonCodecTest(ChessTimerMapCodecTest)

/** Companion object of [[ChessTimerMapCodecTest]]. */
object ChessTimerMapCodecTest extends BsonCodecTest.BsonCodecData[ChessTimerMap]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[ChessTimerMap](
      name = "ChessTimerMap",
      sample = bson {
        "white" :: DurationCodecTest.dataSample
        "black" :: DurationCodecTest.dataSample
      },
      expected = Map(
        LegacyTeam.WHITE -> DurationCodecTest.dataSample,
        LegacyTeam.BLACK -> DurationCodecTest.dataSample
      )
    ),
    DecodeSample[ChessTimerMap](
      name = "Partial ChessTimerMap",
      sample = bson {
        "white" :: DurationCodecTest.dataSample
        "black" :: BsonNull.VALUE
      },
      expected = Map(LegacyTeam.WHITE -> DurationCodecTest.dataSample)
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq()
