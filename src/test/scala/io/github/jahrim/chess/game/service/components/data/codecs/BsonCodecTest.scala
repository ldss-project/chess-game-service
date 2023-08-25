package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest.BsonCodecData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonValue
import test.AbstractTest

import scala.reflect.{ClassTag, classTag}

/** An [[AbstractTest]] for a codec with the specified name. */
abstract class BsonCodecTest[C](data: BsonCodecData[C]) extends AbstractTest:
  describe(s"A BsonDecoder for ${data.typeName}") {
    data.decodeSamples.foreach { sample =>
      it(s"should properly decode sample '${sample.name}'") {
        assert(sample.properlyDecoded)
      }
    }
    data.wrongDecodeSamples.foreach { sample =>
      it(s"should throw an error when decoding sample '${sample.name}'") {
        assertThrows[Exception](sample.decode())
      }
    }
  }

  describe(s"A BsonEncoder for ${data.typeName}") {
    data.encodeSamples.foreach { sample =>
      it(s"should properly encode sample '${sample.name}'") {
        assert(sample.properlyEncoded)
      }
    }
  }

/** Companion object of [[BsonCodecTest]]. */
object BsonCodecTest:
  /**
   * A set of data samples used for testing in a [[BsonCodecTest]].
   *
   * @tparam C the type of data samples.
   */
  abstract class BsonCodecData[C: ClassTag]:
    /** An [[Iterable]] of [[DecodeSample]]s. */
    type DecodeSampleIterable = Iterable[DecodeSample[C]]

    /** An [[Iterable]] of [[WrongDecodeSample]]s. */
    type WrongDecodeSampleIterable = Iterable[WrongDecodeSample[C]]

    /** An [[Iterable]] of [[EncodeSample]]s. */
    type EncodeSampleIterable = Iterable[EncodeSample[C]]

    /** @return the name of the type of this data. */
    def typeName: String = classTag[C].runtimeClass.getSimpleName

    /**
     * The proper samples used for testing the decoding of this type of data.
     *
     * @note the [[encodeSamples]] are automatically inferred from these samples
     *       if not overridden.
     */
    def decodeSamples: DecodeSampleIterable

    /** The wrong samples used for testing the decoding of this type of data. */
    def wrongDecodeSamples: WrongDecodeSampleIterable

    /**
     * The proper samples used for testing the encoding of this type of data.
     *
     * @note these samples are automatically inferred from the [[decodeSamples]]
     *       if not overridden.
     */
    def encodeSamples: EncodeSampleIterable = decodeSamples.map(_.asEncodeSample)

    /** @return a sample of this type of data. */
    def dataSample: C = decodeSamples.head.expected

    /** @return a sample of this type of data as [[BsonValue]]. */
    def bsonSample: BsonValue = decodeSamples.head.sample

    /**
     * A wrapper used for testing the decoding of a data sample.
     *
     * @param name the name of the data sample.
     * @param sample a [[BsonValue]] representing the data sample.
     * @param expected the outcome expected from decoding the specified data sample.
     */
    protected case class DecodeSample[+D <: C](name: String, sample: BsonValue, expected: D)(using
        BsonDecoder[D],
        BsonEncoder[D]
    ):
      /**
       * @return true if the decoded data sample is equal to the expected outcome,
       *         false otherwise.
       */
      def properlyDecoded: Boolean =
        val decoded: D = sample.as[D]
        val result: Boolean = decoded == expected
        if !result then
          println(s"\n### $name")
          println(s"sample: $sample")
          println(s"expected: $expected")
          println(s"was: $decoded.")
        result

      /** @return an [[EncodeSample]] for testing the reverse operation of this [[EncodeSample]]. */
      def asEncodeSample: EncodeSample[D] = EncodeSample(name, expected, sample)

    /**
     * As [[DecodeSample]] but with no expected outcomes other than the decoding
     * to throw an error.
     */
    protected case class WrongDecodeSample[+D <: C](name: String, sample: BsonValue)(using
        BsonDecoder[D]
    ):
      /** Decode the data sample, throwing if it wasn't possible. */
      def decode(): Unit = sample.as[D]

    /**
     * A wrapper used for testing the encoding of a data sample.
     *
     * @param name the name of the data sample.
     * @param sample the data sample.
     * @param expected the outcome expected from encoding the specified data sample
     *                 into a [[BsonValue]].
     */
    protected case class EncodeSample[+D <: C](name: String, sample: D, expected: BsonValue)(using
        BsonEncoder[D]
    ):
      /**
       * @return true if the encoded data sample is equal to the expected outcome,
       *         false otherwise.
       */
      def properlyEncoded: Boolean =
        val encoded: BsonValue = sample.asBson
        val result: Boolean = encoded == expected
        if !result then
          println(s"\n### $name")
          println(s"sample: $sample")
          println(s"expected: $expected")
          println(s"was: $encoded.")
        result
