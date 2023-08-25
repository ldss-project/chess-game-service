package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.MoveHistoryCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.MoveHistory
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}

/** A [[BsonCodecTest]] for [[MoveHistoryCodec]]. */
class MoveHistoryCodecTest extends BsonCodecTest(MoveHistoryCodecTest)

/** Companion object of [[MoveHistoryCodecTest]]. */
object MoveHistoryCodecTest extends BsonCodecTest.BsonCodecData[MoveHistory]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[MoveHistory](
      name = "MoveHistory",
      sample = bson {
        "entries" :* (
          bson {
            "piece" :: LegacyPieceCodecTest.bsonSample
            "move" :: LegacyMoveCodecTest.bsonSample
          },
          bson {
            "piece" :: LegacyPieceCodecTest.bsonSample
            "move" :: LegacyMoveCodecTest.bsonSample
          }
        )
      },
      expected = MoveHistory(
        Seq(
          LegacyPieceCodecTest.dataSample -> LegacyMoveCodecTest.dataSample,
          LegacyPieceCodecTest.dataSample -> LegacyMoveCodecTest.dataSample
        )
      )
    ),
    DecodeSample[MoveHistory](
      name = "Empty MoveHistory",
      sample = bson { "entries" :: Seq() },
      expected = MoveHistory.empty
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[MoveHistory](
      name = "MoveHistory no pieces",
      sample = bson {
        "entries" :* bson {
          "move" :: LegacyMoveCodecTest.bsonSample
        }
      }
    ),
    WrongDecodeSample[MoveHistory](
      name = "Move with no moves",
      sample = bson {
        "entries" :* bson {
          "piece" :: LegacyPieceCodecTest.bsonSample
        }
      }
    )
  )
