package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.GameSituationCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameSituation
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[GameSituationCodec]]. */
class GameSituationCodecTest extends BsonCodecTest(GameSituationCodecTest)

/** Companion object of [[GameSituationCodecTest]]. */
object GameSituationCodecTest extends BsonCodecTest.BsonCodecData[GameSituation]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[GameSituation](
      name = "None",
      sample = bson { "type" :: "None" },
      expected = GameSituation.None
    ),
    DecodeSample[GameSituation](
      name = "Check",
      sample = bson { "type" :: "Check" },
      expected = GameSituation.Check
    ),
    DecodeSample[GameSituation](
      name = "Checkmate",
      sample = bson { "type" :: "Checkmate" },
      expected = GameSituation.Checkmate
    ),
    DecodeSample[GameSituation](
      name = "Stale",
      sample = bson { "type" :: "Stale" },
      expected = GameSituation.Stale
    ),
    DecodeSample[GameSituation](
      name = "Promotion",
      sample = bson {
        "type" :: "Promotion"
        "promotingPawnPosition" :: LegacyPositionCodecTest.bsonSample
      },
      expected = GameSituation.Promotion(LegacyPositionCodecTest.dataSample)
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[GameSituation](
      name = "Unknown GameSituation",
      sample = bson { "type" :: "UnknownGameSituation" }
    ),
    WrongDecodeSample[GameSituation](
      name = "Promotion without promoting pawn position",
      sample = bson { "type" :: "Promotion" }
    )
  )
