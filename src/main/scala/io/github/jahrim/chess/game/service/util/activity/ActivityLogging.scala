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
package io.github.jahrim.chess.game.service.util.activity

import io.github.jahrim.chess.game.service.util.vertx.FutureExtension.future
import io.github.jahrim.hexarc.logging.Loggers
import io.vertx.core.{Future, Vertx}

import scala.util.{Failure, Success, Try}

/** Mixin that allows the child class to monitor the execution of its activities. */
trait ActivityLogging:
  private var _defaultActivityLogger: LoggingFunction = Loggers.nop.info(_)

  /**
   * @return the default [[LoggingFunction]] used to monitor the execution
   *         of the activities of this class.
   */
  protected def defaultActivityLogger: LoggingFunction = Loggers.nop.info(_)

  /**
   * Execute the specified activity, monitoring its start, failure
   * or completion.
   *
   * @param activityName   the name of the specified activity.
   * @param activityLogger the [[LoggingFunction]] used to monitor the execution
   *                       of the specified activity (default: [[defaultActivityLogger]]).
   * @param activity       the specified activity.
   * @tparam T the return type of the specified activity.
   * @return a [[Success]] containing the result of the specified activity,
   *         if it was successful; a [[Failure]] containing the [[Throwable]]
   *         that caused the activity to fail, otherwise.
   */
  def tryActivity[T](
      activityName: String,
      activityLogger: LoggingFunction = defaultActivityLogger
  )(activity: => T): Try[T] =
    activityLogger(s"Started: $activityName.")
    val result: Try[T] = Try(activity)
    result match
      case Success(_) => activityLogger(s"Completed: $activityName.")
      case Failure(_) => activityLogger(s"Failed: $activityName.")
    result

  /**
   * As [[tryActivity]] but does not wrap the result of the specified activity in a
   * [[Try]].
   *
   * @return the result of the specified activity, if it was successful; throws the
   *         [[Throwable]] that caused the activity to fail, otherwise.
   */
  def activity[T](
      activityName: String,
      activityLogger: LoggingFunction = defaultActivityLogger
  )(activity: => T): T =
    this.tryActivity(activityName, activityLogger)(activity).get

  /**
   * As [[tryActivity]] but executes asynchronously.
   *
   * @return a [[Future]] containing the result of the specified activity,
   *         if it was successful; a [[Future]] containing the [[Throwable]]
   *         that caused the activity to fail, otherwise.
   */
  def asyncActivity[T](
      activityName: String,
      activityLogger: LoggingFunction = defaultActivityLogger
  )(activity: => T)(using Vertx): Future[T] =
    future(this.activity(activityName, activityLogger)(activity))

  /**
   * As [[tryActivity]] but executes asynchronously.
   *
   * @return a [[Future]] containing the result of the specified activity,
   *         if it was successful; a [[Future]] containing the [[Throwable]]
   *         that caused the activity to fail, otherwise.
   */
  def asyncActivityFlatten[T](
      activityName: String,
      activityLogger: LoggingFunction = defaultActivityLogger
  )(activity: => Future[T])(using Vertx): Future[T] =
    future(activityLogger(s"Started: $activityName."))
      .compose(_ => activity)
      .onSuccess(_ => activityLogger(s"Completed: $activityName."))
      .onFailure(_ => activityLogger(s"Failed: $activityName."))
