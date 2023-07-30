package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson

object Codecs:
  export TimeConstraintCodec.given
  export TimeConstraintTypeDataCodec.given
  export GameConfigurationCodec.given
  export ChessboardStatusCodec.given
  export DurationCodec.given
  export PieceTypeDataCodec.given
  export PieceCodec.given
  export PlayerCodec.given
  export FileCodec.given
  export RankCodec.given
  export PositionCodec.given
  export RookCodec.given
  export CastlingCodec.given
