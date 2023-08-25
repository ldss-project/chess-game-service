package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.codecs.BsonCodecTest
import io.github.jahrim.chess.game.service.components.data.codecs.ServerSituationCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerSituation
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonString

/** A [[BsonCodecTest]] for [[ServerSituationCodec]]. */
class ServerSituationCodecTest extends BsonCodecTest(ServerSituationCodecTest)

/** Companion object of [[ServerSituationCodecTest]]. */
object ServerSituationCodecTest extends BsonCodecTest.BsonCodecData[ServerSituation]:
  override def decodeSamples: DecodeSampleIterable = Seq(
    DecodeSample[ServerSituation](
      name = "NotConfigured",
      sample = BsonString("NotConfigured"),
      expected = ServerSituation.NotConfigured
    ),
    DecodeSample[ServerSituation](
      name = "WaitingForPlayers",
      sample = BsonString("WaitingForPlayers"),
      expected = ServerSituation.WaitingForPlayers
    ),
    DecodeSample[ServerSituation](
      name = "Ready",
      sample = BsonString("Ready"),
      expected = ServerSituation.Ready
    ),
    DecodeSample[ServerSituation](
      name = "Running",
      sample = BsonString("Running"),
      expected = ServerSituation.Running
    ),
    DecodeSample[ServerSituation](
      name = "Terminated",
      sample = BsonString("Terminated"),
      expected = ServerSituation.Terminated
    )
  )

  override def wrongDecodeSamples: WrongDecodeSampleIterable = Seq(
    WrongDecodeSample[ServerSituation](
      name = "Unknown ServerSituation",
      sample = BsonString("UnknownServerSituation")
    )
  )
