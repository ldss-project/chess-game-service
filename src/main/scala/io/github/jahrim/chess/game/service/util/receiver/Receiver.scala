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
