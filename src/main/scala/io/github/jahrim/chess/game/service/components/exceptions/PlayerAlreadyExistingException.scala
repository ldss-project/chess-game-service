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
package io.github.jahrim.chess.game.service.components.exceptions

import io.github.chess.engine.model.game.Team as LegacyTeam

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * join a chess game as a player of a specified team when a player
 * of that team already exists.
 *
 * @param message a detailed description of the [[Exception]].
 */
class PlayerAlreadyExistingException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[PlayerAlreadyExistingException]]. */
object PlayerAlreadyExistingException:
  def apply(gameId: String, team: LegacyTeam): PlayerAlreadyExistingException =
    new PlayerAlreadyExistingException(
      s"Player '$team' already existing in game with id '$gameId'."
    )
