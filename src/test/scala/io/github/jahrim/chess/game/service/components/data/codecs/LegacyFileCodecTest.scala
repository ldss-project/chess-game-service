package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.File as LegacyFile
import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyFileCodec.given
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[LegacyFileCodec]]. */
class LegacyFileCodecTest extends BsonCodecTest(LegacyFileCodecTest)

/** Companion object of [[LegacyFileCodecTest]]. */
object LegacyFileCodecTest extends BsonCodecTest.BsonCodecData[LegacyFile]:
  override def decodeSamples: DecodeSampleIterable =
    LegacyFile.values.map(file =>
      DecodeSample[LegacyFile](
        name = s"File ${file.productPrefix}",
        sample = BsonString(file.productPrefix),
        expected = file
      ),
    )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[LegacyFile](
      name = "Unknown File",
      sample = BsonString("UnknownFile")
    )
  )
