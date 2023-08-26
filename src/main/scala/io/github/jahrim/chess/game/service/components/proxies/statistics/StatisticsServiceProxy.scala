package io.github.jahrim.chess.game.service.components.proxies.statistics

import io.github.jahrim.chess.game.service.components.data.codecs.Codecs.given
import io.github.jahrim.chess.game.service.components.exceptions.InternalServerException
import io.github.jahrim.chess.game.service.components.proxies.statistics.exceptions.StatisticsServiceException
import io.github.jahrim.chess.game.service.util.vertx.FutureExtension.*
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.{Future, Vertx}
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.{HttpResponse, WebClient, WebClientOptions}

import java.net.URL
import scala.util.{Failure, Success, Try}

/** A proxy for communicating with the statistics service. */
trait StatisticsServiceProxy:
  /**
   * Add the specified score to the scores of the specified user.
   *
   * @param username the name of the specified user.
   * @param hasWon   the specified score.
   *                 This is an [[Option]] containing true, if the user won the
   *                 match; false otherwise. If the [[Option]] is empty, the
   *                 result of the match will be registered in the scores of the
   *                 user as a par.
   * @return a [[Future]] completing when the specified score has
   *         been successfully added to the scores of the specified
   *         user.
   */
  def addNewScore(username: String, hasWon: Option[Boolean]): Future[Unit]

/** Companion object of [[StatisticsServiceProxy]]. */
object StatisticsServiceProxy:
  /** The http protocol used for communicating with the statistics service. */
  private val HttpProtocol: String = "http"

  /**
   * @param serviceHost the host of the statistics service.
   * @param vertx the [[Vertx]] instance used for http interactions.
   * @return a new [[StatisticsServiceProxy]].
   */
  def apply(serviceHost: String, vertx: Vertx = Vertx.vertx()): StatisticsServiceProxy =
    BasicStatisticsServiceProxy(URL(s"$HttpProtocol://$serviceHost"), vertx)

  /**
   * @return a stub implementation of the [[StatisticsServiceProxy]],
   *         acting as a placeholder where a [[StatisticsServiceProxy]]
   *         is needed by cannot be provided.
   *
   *         Method calls to the stub implementation do nothing.
   */
  def stub: StatisticsServiceProxy = StatisticsServiceProxyStub()

  /** Basic implementation of [[StatisticsServiceProxy]]. */
  private case class BasicStatisticsServiceProxy(
      serviceURL: URL,
      vertx: Vertx = Vertx.vertx()
  ) extends StatisticsServiceProxy:
    /**
     * The [[Vertx]] instance where the asynchronous activities of this
     * proxy will be executed.
     */
    protected given Vertx = vertx

    private val _client: WebClient =
      WebClient.create(
        vertx,
        WebClientOptions()
          .setDefaultHost(serviceURL.getHost)
          .setDefaultPort(serviceURL.getPort)
      )

    override def addNewScore(username: String, hasWon: Option[Boolean]): Future[Unit] =
      this._client
        .post(s"/score/$username")
        .sendJson(
          bson {
            "score" :# {
              hasWon.foreach("hasWon" :: _)
            }
          }.as[JsonObject]
        )
        .followedBy {
          case success if success.statusCode == 200 =>
          case failure                              => failureHandler(failure)
        }

    /**
     * Analyze the cause of the failure of the specified [[HttpResponse]]
     * throwing a corresponding [[StatisticsServiceException]].
     *
     * @param response the specified [[HttpResponse]].
     * @throws InternalServerException if the cause of the failure could
     *                                 not be mapped into a [[StatisticsServiceException]].
     */
    private def failureHandler(response: HttpResponse[Buffer]): Unit =
      Try(response.bodyAsJsonObject.asBson.asDocument.as[StatisticsServiceException]) match
        case Success(statisticsException) => throw statisticsException
        case Failure(unknownException) =>
          InternalServerException(StatisticsServiceException(response.statusMessage))

  /**
   * A stub implementation of [[StatisticsServiceProxy]].
   *
   * Method calls to the [[StatisticsServiceProxyStub]] do nothing.
   */
  private case class StatisticsServiceProxyStub() extends StatisticsServiceProxy:
    override def addNewScore(username: String, hasWon: Option[Boolean]): Future[Unit] =
      Future.succeededFuture()
