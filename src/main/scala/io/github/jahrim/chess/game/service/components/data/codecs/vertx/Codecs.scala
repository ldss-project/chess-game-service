/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele, Kentpayeva Madina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.jahrim.chess.game.service.components.data.codecs.vertx

import io.github.jahrim.chess.game.service.components.data.codecs.Codecs.given
import io.github.jahrim.chess.game.service.components.events.*
import io.github.jahrim.hexarc.logging.Loggers
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.Vertx
import io.vertx.core.eventbus.MessageCodec
import org.bson.BsonDocument

import scala.util.{Failure, Success, Try}

/** [[Vertx]] event-bus codecs of the chess game service. */
object Codecs:
  /**
   * An entry that can be registered to the codecs used by the [[Vertx]]
   * event-bus during the serialization and deserialization of events.
   *
   * @param clazz the class (de)serialized by this [[CodecEntry]].
   * @param codec the codec used for (de)serialization.
   * @tparam A the type of objects (de)serialized by this [[CodecEntry]].
   */
  private case class CodecEntry[A](clazz: Class[A], codec: MessageCodec[A, ?])

  /** A collection of the [[CodecEntry]]s used by the chess game service. */
  private val CodecEntries: Iterable[CodecEntry[?]] = Seq(
    CodecEntry(classOf[BsonDocument], BsonCodec()),
    CodecEntry(classOf[ChessGameServiceEvent], EventCodec[ChessGameServiceEvent]()),
    CodecEntry(
      classOf[LoggingEvent],
      EventWithPayloadCodec[
        LoggingEvent,
        LoggingEvent#PayloadType
      ]()
    ),
    CodecEntry(classOf[ServerStateUpdateEvent], EventCodec[ServerStateUpdateEvent]()),
    CodecEntry(
      classOf[ServerSituationUpdateEvent],
      EventWithPayloadCodec[
        ServerSituationUpdateEvent,
        ServerSituationUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[ServerErrorUpdateEvent],
      EventWithPayloadCodec[
        ServerErrorUpdateEvent,
        ServerErrorUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[SubscriptionUpdateEvent],
      EventWithPayloadCodec[
        SubscriptionUpdateEvent,
        SubscriptionUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(classOf[GameStateUpdateEvent], EventCodec[GameStateUpdateEvent]()),
    CodecEntry(
      classOf[ChessboardUpdateEvent],
      EventWithPayloadCodec[
        ChessboardUpdateEvent,
        ChessboardUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[GameOverUpdateEvent],
      EventWithPayloadCodec[
        GameOverUpdateEvent,
        GameOverUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[GameSituationUpdateEvent],
      EventWithPayloadCodec[
        GameSituationUpdateEvent,
        GameSituationUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[MoveHistoryUpdateEvent],
      EventWithPayloadCodec[
        MoveHistoryUpdateEvent,
        MoveHistoryUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[PlayerUpdateEvent],
      EventWithPayloadCodec[
        PlayerUpdateEvent,
        PlayerUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[BlackPlayerUpdateEvent],
      EventWithPayloadCodec[
        BlackPlayerUpdateEvent,
        BlackPlayerUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[WhitePlayerUpdateEvent],
      EventWithPayloadCodec[
        WhitePlayerUpdateEvent,
        WhitePlayerUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[TimerUpdateEvent],
      EventWithPayloadCodec[
        TimerUpdateEvent,
        TimerUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[BlackTimerUpdateEvent],
      EventWithPayloadCodec[
        BlackTimerUpdateEvent,
        BlackTimerUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[WhiteTimerUpdateEvent],
      EventWithPayloadCodec[
        WhiteTimerUpdateEvent,
        WhiteTimerUpdateEvent#PayloadType
      ]()
    ),
    CodecEntry(
      classOf[TurnUpdateEvent],
      EventWithPayloadCodec[
        TurnUpdateEvent,
        TurnUpdateEvent#PayloadType
      ]()
    )
  )

  /**
   * Register these [[Vertx]] codecs into the event-bus of the
   * specified [[Vertx]] instance.
   *
   * @param vertx the specified [[Vertx]] instance.
   */
  def registerInto(vertx: Vertx): Unit =
    CodecEntries.foreach(entry =>
      Try(vertx.eventBus.registerDefaultCodec(entry.clazz, entry.codec))
    )
