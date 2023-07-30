package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.jahrim.chess.game.service.components.data.ChessboardStatusData
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import org.bson.conversions.Bson
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import PieceDataCodec.given
import MoveDataCodec.given
import io.github.jahrim.chess.game.service.components.data.PieceData
import io.github.jahrim.chess.game.service.components.data.MoveData

/** [[Bson]] codec for [[ChessboardStatusData]] */
object ChessboardStatusDataCodec:

  /** A given [[BsonDocumentDecoder]] for [[ChessboardStatusData]] */
  given BsonDocumentDecoder[ChessboardStatusData] = bson =>
    ChessboardStatusData(
      pieces = bson.require("pieces").as[Seq[PieceData]],
      moves = bson.require("moves").as[Seq[MoveData]]
    )

  /** A given [[BsonDocumentEncoder]] for [[ChessboardStatusData]] */
  given BsonDocumentEncoder[ChessboardStatusData] = csd =>
    bson {
      "pieces" :: csd.pieces
      "moves" :: csd.moves
    }
