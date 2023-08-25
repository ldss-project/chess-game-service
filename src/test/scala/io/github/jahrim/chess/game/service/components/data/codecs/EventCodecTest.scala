package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.EventCodec.given
import io.github.jahrim.chess.game.service.components.events.{
  ChessGameServiceEvent,
  Event,
  GameStateUpdateEvent
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[EventCodec]]. */
class EventCodecTest extends BsonCodecTest(EventCodecTest)

/** Companion object of [[EventCodecTest]]. */
object EventCodecTest extends BsonCodecTest.BsonCodecData[Event]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[Event](
      name = "Event",
      sample = bson { "type" :: "ChessGameServiceEvent" },
      expected = ChessGameServiceEvent()
    ),
    DecodeSample[Event](
      name = "Specific Event",
      sample = bson { "type" :: "GameStateUpdateEvent" },
      expected = GameStateUpdateEvent()
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[Event](
      name = "Unknown Event",
      sample = bson { "type" :: "UnknownEvent" }
    )
  )
