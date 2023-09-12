/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele, Kentpayeva Madina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package io.github.jahrim.chess.game.service.components.data.codecs

import io.github.chess.engine.model.board.{
  File as LegacyFile,
  Position as LegacyPosition,
  Rank as LegacyRank
}
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyFileCodec.given
import io.github.jahrim.chess.game.service.components.data.codecs.LegacyRankCodec.given
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDecoder,
  BsonDocumentDecoder,
  BsonDocumentEncoder,
  BsonEncoder
}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** [[Bson]] codec for [[LegacyPosition LegacyPosition]]. */
object LegacyPositionCodec:
  /** A given [[BsonDecoder]] for [[LegacyPosition LegacyPosition]]. */
  given legacyPositionDecoder: BsonDocumentDecoder[LegacyPosition] = bson =>
    LegacyPosition(
      bson.require("file").as[LegacyFile],
      bson.require("rank").as[LegacyRank]
    )

  /** A given [[BsonEncoder]] for [[LegacyPosition LegacyPosition]]. */
  given legacyPositionEncoder: BsonDocumentEncoder[LegacyPosition] = position =>
    bson {
      "file" :: position.file
      "rank" :: position.rank
    }
