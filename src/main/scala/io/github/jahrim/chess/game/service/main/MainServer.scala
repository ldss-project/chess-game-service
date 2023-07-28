package io.github.jahrim.chess.game.service.main

import io.vertx.core.AbstractVerticle
import io.vertx.core.Launcher

class MainServer extends AbstractVerticle:

  def main(args: Array[String]): Unit =
    Launcher.executeCommand("run", MainClient.super.getClass.getName)

    def start(): Unit =
      vertx.createHttpServer.webSocketHandler(websocket =>
        websocket.handler(websocket.writeBinaryMessage)).requestHandler(req =>
        if (req.uri.equals("/"))
          req.response
      ).listen(8080)
