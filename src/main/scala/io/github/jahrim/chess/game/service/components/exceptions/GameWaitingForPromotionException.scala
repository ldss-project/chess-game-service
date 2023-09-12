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

/**
 * A [[ChessGameServiceException]] triggered when a user attempts to
 * perform an action that requires a chess game not to be waiting
 * for a promotion, but the chess game is waiting for a promotion.
 *
 * @param message a detailed description of the [[Exception]].
 */
class GameWaitingForPromotionException(message: String) extends ChessGameServiceException(message)

/** Companion object of [[GameWaitingForPromotionException]]. */
object GameWaitingForPromotionException:
  def apply(gameId: String): GameWaitingForPromotionException =
    new GameWaitingForPromotionException(
      s"Game '$gameId' rejected the request: waiting for promotion."
    )
