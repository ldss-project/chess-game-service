package io.github.jahrim.chess.game.service.components.data.codecs

import org.bson.conversions.Bson

object Codecs:
  export TimeConstraintDataCodec.given
  export TimeConstraintTypeDataCodec.given
  export GameConfigurationDataCodec.given
  export ChessboardStatusDataCodec.given
  export DurationCodec.given
  export PieceTypeDataCodec.given
  export PieceDataCodec.given
  export PlayerDataCodec.given
  export FileCodec.given
  export RankCodec.given
  export PositionDataCodec.given
  export RookDataCodec.given
  export CastlingDataCodec.given
