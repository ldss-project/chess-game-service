package io.github.jahrim.chess.game.service.components.data

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}

case class ChessboardStatusData(
    pieces: Seq[PieceData],
    moves: Seq[MoveData]
)
