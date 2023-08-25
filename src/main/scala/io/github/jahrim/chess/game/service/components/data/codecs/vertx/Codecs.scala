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
