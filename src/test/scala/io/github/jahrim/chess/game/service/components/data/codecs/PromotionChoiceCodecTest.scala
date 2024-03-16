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