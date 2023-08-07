package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.*
import io.github.jahrim.chess.game.service.components.data.codecs.GameOverDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.SituationDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.TurnDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.EnPassantDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.MoveTypeDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PlayerPerspectiveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.PositionDataCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.concurrent.duration.Duration

/** [[Bson]] codec for [[StateData]]. */
object StateDataCodec:
  /** A given [[BsonDocumentDecoder]] for [[StateData]]. */
  given bsonToState: BsonDocumentDecoder[StateData] = bson =>
    StateData(
      turn = bson.require("turn").as[TurnData],
      situation = bson("situation").map(_.as[SituationData]),
      white = bson.require("white").as[PlayerPerspectiveData],
      black = bson.require("black").as[PlayerPerspectiveData],
      gameOver = bson("gameOver").map(_.as[GameOverData])
    )

  /** A given [[BsonDocumentEncoder]] for [[StateData]]. */
  given stateToBson: BsonDocumentEncoder[StateData] = s =>
    bson {
      "turn" :: s.turn
      s.situation.foreach { "situation" :: _ }
      "white" :: s.white
      "black" :: s.black
      s.gameOver.foreach { "gameOver" :: _ }
    }
