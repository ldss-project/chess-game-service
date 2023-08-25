package io.github.jahrim.chess.game.service.components.adapters.http.handlers

import io.vertx.core.Handler
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

/** An handler for websocket connections. */
@FunctionalInterface
trait WebsocketHandler extends Handler[SockJSSocket]
