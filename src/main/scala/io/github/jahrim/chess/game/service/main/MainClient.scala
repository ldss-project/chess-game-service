package io.github.jahrim.chess.game.service.main

import io.vertx.core.AbstractVerticle
import io.vertx.core.Launcher
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient

class MainClient extends AbstractVerticle:

  def main(args: Array[String]): Unit =
    Launcher.executeCommand("run", MainClient.super.getClass.getName)

    def start(): Unit =
      val client = vertx.createHttpClient
      client.webSocket(8080, "localhost", "/uri").onSuccess((webSocket) =>
        webSocket.handler((data) =>
          System.out.println("Received data " + data.toString("ISO-8859-1"))
          client.close

        )
        webSocket.writeBinaryMessage(Buffer.buffer("Hello world"))

      ).onFailure(Throwable.printStackTrace)
