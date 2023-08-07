package io.github.jahrim.chess.game.service.components.data.codecs

import test.AbstractTest

abstract class CodecTest(codecName: String) extends AbstractTest:
  def decodeCorrectBsonTest(): Unit
  def decodeWrongBsonTest(): Unit
  def encodeTest(): Unit

  describe(s"A BsonDecoder for $codecName") {
    it("should decode properly a correct bson") {
      decodeCorrectBsonTest()
    }
    it("should throw an error when decoding a wrong bson") {
      decodeWrongBsonTest()
    }
  }

  describe(s"sA BsonEncoder for $codecName") {
    it(s"should encode properly a $codecName") {
      encodeTest()
    }
  }
