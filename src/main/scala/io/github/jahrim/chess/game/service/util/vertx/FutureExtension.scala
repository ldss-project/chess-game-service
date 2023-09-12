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
package io.github.jahrim.chess.game.service.util.vertx

import io.vertx.core.{Future, Vertx}

import scala.util.Try

/** An extension for [[Future VertxFuture]]s. */
object FutureExtension:
  /**
   * A supplier of given [[Vertx]] instances to run the [[Future]]s
   * created with this extension.
   */
  given vertxSupplier: Vertx = Vertx.vertx()

  /**
   * Creates a new [[Future VertxFuture]] from the specified activity, executed
   * asynchronously by the given [[Vertx]] instance.
   *
   * @param activity the specified activity.
   * @param vertx the given vertx instance.
   * @return a new [[Future VertxFuture]] containing the result of the specified
   *         activity when completed; containing the first [[Throwable]] thrown
   *         during the activity when failed.
   */
  def future[T](activity: => T)(using vertx: Vertx): Future[T] =
    Future.future(promise =>
      vertx.runOnContext(_ => Try(activity).fold(promise.fail, promise.complete))
    )

  extension [T](self: Future[T]) {

    /**
     * Creates a new [[Future VertxFuture]] from the specified activity, executed
     * asynchronously by the given [[Vertx]] instance, consuming the result of this
     * [[Future VertxFuture]].
     *
     * @param activity the specified activity.
     * @param vertx the given vertx instance.
     * @tparam T2 the type of the result of the specified activity.
     * @return a new [[Future VertxFuture]] containing the result of the specified
     *         activity when completed; containing the first [[Throwable]] thrown
     *         during the activity when failed.
     *
     * @note If the activity of this [[Future VertxFuture]] will fail, then the
     *       cause of the failure will be propagated without executing the specified
     *       activity.
     */
    def followedBy[T2](activity: T => T2)(using vertx: Vertx): Future[T2] =
      self.compose(result => future(activity(result)))
  }
