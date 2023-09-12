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
import io.github.jahrim.chess.game.service.components.data.codecs.DurationCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.OptionCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.{BsonNull, BsonString}

import scala.concurrent.duration.Duration

/** A [[BsonCodecTest]] for [[OptionCodec]]. */
class OptionCodecTest extends BsonCodecTest(OptionCodecTest)

/** Companion object of [[OptionCodecTest]]. */
object OptionCodecTest extends BsonCodecTest.BsonCodecData[Option[?]]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[Option[String]](
      name = "Some of value",
      sample = BsonString("Value"),
      expected = Some("Value")
    ),
    DecodeSample[Option[Duration]](
      name = "Some of document",
      sample = DurationCodecTest.bsonSample,
      expected = Some(DurationCodecTest.dataSample)
    ),
    DecodeSample[Option[Duration]](
      name = "None",
      sample = BsonNull.VALUE,
      expected = None
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq()
