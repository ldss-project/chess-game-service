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
package io.github.jahrim.chess.game.service.components.events

import io.github.jahrim.chess.game.service.components.events.Event.searchAddressHierarchy

import scala.annotation.tailrec
import scala.reflect.{ClassTag, classTag}

/** An event directed to a set of addresses. */
trait Event extends Equals:
  /** @return the name of this [[Event]]. */
  def name: String = this.getClass.getSimpleName

  override def toString: String = s"Event{name: $name}"
  override def canEqual(that: Any): Boolean = that.isInstanceOf[Event]
  override def equals(that: Any): Boolean = that match
    case other: Event => this.canEqual(other) && this.name == other.name
    case _            => false
  override def hashCode(): Int = 31 * 19 + name.##

/** Companion object of [[Event]]. */
object Event:
  /**
   * An [[Event]] carrying some data.
   *
   * @tparam D the type of data carried by this [[Event]].
   */
  trait Payload[D](val payload: D) extends Equals:
    self: Event =>

    /** The type of data carried by this [[Event]]. */
    type PayloadType = D

    override def toString: String = s"Event{name: $name, payload: $payload}"
    override def canEqual(that: Any): Boolean = that.isInstanceOf[Event with Payload[_]]
    override def equals(that: Any): Boolean = that match
      case other: Event with Payload[_] =>
        this.canEqual(other) && this.name == other.name && this.payload == other.payload
      case _ => false
    override def hashCode(): Int = 31 * 19 + this.name.## + this.payload.##

  /**
   * Alias for [[Event.addressesOf Event.addressesOf[E].head]].
   * @tparam E the type of the specified [[Event]].
   * @return the most specific address to which the specified [[Event]]
   *         is directed.
   */
  def addressOf[E <: Event: ClassTag]: String = addressesOf[E].head

  /**
   * @tparam E the type of the specified [[Event]].
   * @return the addresses to which the specified [[Event]] is directed.
   * @note By default, these are the addresses of all the [[Event]]s
   *       extended by the specified [[Event]], ordered by decreasing
   *       specificity (from the address of the most concrete class to
   *       the address of the most abstract class).
   */
  def addressesOf[E <: Event: ClassTag]: Seq[String] =
    searchAddressHierarchy(classTag[E].runtimeClass)

  /**
   * @param eventClass the [[Class]] of the specified [[Event]].
   * @return a sequence of the addresses of the parents of the specified [[Event]],
   *         including itself, ordered by decreasing specificity.
   */
  private def searchAddressHierarchy(eventClass: Class[_]): Seq[String] =
    @tailrec
    def searchStep(eventClass: Class[_], addresses: Seq[String]): Seq[String] =
      if eventClass != classOf[Object] then
        searchStep(
          eventClass.getSuperclass,
          (addresses :+ "").map(address => s"/${eventClass.getSimpleName}$address")
        )
      else addresses

    searchStep(eventClass, Seq())
