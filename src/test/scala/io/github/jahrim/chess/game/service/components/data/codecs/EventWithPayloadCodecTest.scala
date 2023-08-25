package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.EventWithPayloadCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyTeamCodec.given
import io.github.jahrim.chess.game.service.components.events.{Event, TurnUpdateEvent}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[EventWithPayloadCodec]]. */
class EventWithPayloadCodecTest extends BsonCodecTest(EventWithPayloadCodecTest)

/** Companion object of [[EventWithPayloadCodecTest]]. */
object EventWithPayloadCodecTest extends BsonCodecTest.BsonCodecData[Event.Payload[?]]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[TurnUpdateEvent](
      name = "Event with payload",
      sample = bson {
        "type" :: "TurnUpdateEvent"
        "payload" :: LegacyTeam.WHITE
      },
      expected = TurnUpdateEvent(LegacyTeam.WHITE)
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[TurnUpdateEvent](
      name = "Event with wrong payload",
      sample = bson { "type" :: "TurnUpdateEvent"; "payload" :: "WrongPayload" }
    )
  )
