package io.github.jahrim.chess.game.service.components.data

import io.github.chess.engine.model.moves.Move
import io.github.chess.engine.model.pieces.Piece
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}

case class ChessboardStatusData(
  pieces: Seq[Piece],
  moves: Seq[Move]
)

object ChessboardStatusCodec:
  given BsonDocumentDecoder[ChessboardStatusData] = bson =>
    ChessboardStatusData(
      pieces = bson.require("pieces").as[Seq[Piece]],
      moves = bson.require("moves").as[Seq[Move]]
    )

  given BsonDocumentEncoder[ChessboardStatusData] = csd =>
    bson {
      "pieces" :: csd.pieces
      "moves" :: csd.moves
    }
