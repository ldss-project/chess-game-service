package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{
  ChessBoard as LegacyChessBoard,
  Position as LegacyPosition
}
import io.github.chess.engine.model.pieces.Piece as LegacyPiece
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPieceCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyPositionCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonDocument
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyChessBoard LegacyChessBoard]]. */
object LegacyChessboardCodec:
  /** A given [[BsonDecoder]] for [[LegacyChessBoard LegacyChessBoard]]. */
  given chessboardDecoder: BsonDocumentDecoder[LegacyChessBoard] = bson =>
    LegacyChessBoard.fromMap(
      Map(
        bson
          .require("pieces")
          .as[Seq[BsonDocument]]
          .map(bsonPiece =>
            (
              bsonPiece.require("position").as[LegacyPosition],
              bsonPiece.require("piece").as[LegacyPiece]
            )
          )*
      )
    )

  /** A given [[BsonEncoder]] for [[LegacyChessBoard LegacyChessBoard]]. */
  given chessboardEncoder: BsonDocumentEncoder[LegacyChessBoard] = chessboard =>
    bson {
      "pieces" :: chessboard.pieces.toSeq.map((position, piece) =>
        bson {
          "piece" :: piece
          "position" :: position
        }
      )
    }
