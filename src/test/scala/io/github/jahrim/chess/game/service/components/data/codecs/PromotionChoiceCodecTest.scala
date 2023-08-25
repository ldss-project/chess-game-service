package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.PromotionChoiceCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.PromotionChoice
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[PromotionChoiceCodec]]. */
class PromotionChoiceCodecTest extends BsonCodecTest(PromotionChoiceCodecTest)

/** Companion object of [[PromotionChoiceCodecTest]]. */
object PromotionChoiceCodecTest extends BsonCodecTest.BsonCodecData[PromotionChoice]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[PromotionChoice](
      name = "Knight",
      sample = BsonString("Knight"),
      expected = PromotionChoice.Knight
    ),
    DecodeSample[PromotionChoice](
      name = "Bishop",
      sample = BsonString("Bishop"),
      expected = PromotionChoice.Bishop
    ),
    DecodeSample[PromotionChoice](
      name = "Rook",
      sample = BsonString("Rook"),
      expected = PromotionChoice.Rook
    ),
    DecodeSample[PromotionChoice](
      name = "Queen",
      sample = BsonString("Queen"),
      expected = PromotionChoice.Queen
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[PromotionChoice](
      name = "Unknown PromotionChoice",
      sample = BsonString("UnknownPromotionChoice")
    )
  )
