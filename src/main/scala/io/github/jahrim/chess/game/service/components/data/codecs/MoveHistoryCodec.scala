package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.moves.Move as LegacyMove
import io.github.chess.engine.model.pieces.Piece as LegacyPiece
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyMoveCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPieceCodec.given
import io.github.jahrim.chess.game.service.components.ports.model.game.state.MoveHistory
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonDocument
import org.bson.conversions.Bson

/** [[Bson]] codec for [[MoveHistory]]. */
object MoveHistoryCodec:
  /** A given [[BsonDecoder]] for [[MoveHistory]]. */
  given moveHistoryDecoder: BsonDocumentDecoder[MoveHistory] = bson =>
    MoveHistory(
      bson
        .require("entries")
        .as[Seq[BsonDocument]]
        .map(historyEntry =>
          historyEntry.require("piece").as[LegacyPiece] ->
            historyEntry.require("move").as[LegacyMove]
        )
    )

  /** A given [[BsonEncoder]] for [[MoveHistory]]. */
  given moveHistoryEncoder: BsonDocumentEncoder[MoveHistory] = moveHistory =>
    bson {
      "entries" :: moveHistory.zipWithPieces.map((piece, move) =>
        bson {
          "piece" :: piece
          "move" :: move
        }
      )
    }
