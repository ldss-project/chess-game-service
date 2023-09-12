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
package io.github.jahrim.chess.game.service.util.receiver

import scala.annotation.targetName

/**
 * A receiver of values, based on the observer pattern.
 *
 * Upon receiving a new value, a [[Receiver]] will consume the
 * value, executing a predefined behavior.
 *
 * @tparam T the type of values received by this [[Receiver]].
 */
@FunctionalInterface
trait Receiver[T]:
  /**
   * Send the specified value to this [[Receiver]], triggering
   * its behavior.
   *
   * @param value the specified value.
   */
  def receive(value: => T): Unit = behavior(value)

  /** Shortcut for [[receive]]. */
  @targetName("receiveShortcut")
  def <<(value: => T): Unit = receive(value)

  /**
   * @return the behavior of this [[Receiver]].
   * @note inheriting classes should override this method to
   *       define the behavior of the [[Receiver]].
   */
  protected def behavior: T => Unit

/** Companion object of [[Receiver]]. */
object Receiver:
  /**
   * @param behavior the specified behavior.
   * @tparam T the type of values accepted by the receiver.
   * @return a new [[Receiver]] with the specified behavior.
   */
  def apply[T](behavior: T => Unit): Receiver[T] = BasicReceiver(behavior)

  /**
   * @param behavior the specified behavior.
   * @tparam T the type of values accepted by the receiver.
   * @return a new [[Receiver]] with the specified behavior,
   *         executed atomically.
   */
  def atomic[T](behavior: T => Unit): Receiver[T] = AtomicReceiver(behavior)

  /** Basic implementation of a [[Receiver]]. */
  private case class BasicReceiver[T](override protected val behavior: T => Unit)
      extends Receiver[T]

  /** Implementation of a [[Receiver]] which executes its behavior atomically. */
  private case class AtomicReceiver[T](private val atomicBehavior: T => Unit) extends Receiver[T]:
    override protected def behavior: T => Unit = value =>
      this.synchronized { atomicBehavior(value) }
