package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.JsonObjectCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.vertx.core.json.JsonObject

/** A [[BsonCodecTest]] for [[JsonObjectCodec]]. */
class JsonObjectCodecTest extends BsonCodecTest(JsonObjectCodecTest)

/** Companion object of [[JsonObjectCodecTest]]. */
object JsonObjectCodecTest extends BsonCodecTest.BsonCodecData[JsonObject]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[JsonObject](
      name = "JsonObject",
      sample = bson {
        "booleanField" :: true
        "stringField" :: "value"
        "intField" :: 10
        "longField" :: 10_000_000_000_000L
        "doubleField" :: 0.33d
        "arrayField" :* (1, 2, 3)
        "objectField" :# { "subfield" :: 0 }
      },
      expected = JsonObject(
        """
          |{
          |  "booleanField": true,
          |  "stringField": "value",
          |  "intField": 10,
          |  "longField": 10000000000000,
          |  "doubleField": 0.33,
          |  "arrayField": [1, 2, 3],
          |  "objectField": { "subfield": 0 }
          |}
          |""".stripMargin
      )
    )
  )
  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq()
