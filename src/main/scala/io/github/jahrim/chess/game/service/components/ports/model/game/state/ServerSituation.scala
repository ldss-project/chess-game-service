package io.github.jahrim.chess.game.service.components.ports.model.game.state

/** The situation of a server hosting a chess game. */
enum ServerSituation:
  /**
   * The [[ServerSituation]] where the server is waiting for the chess game
   * to be configured.
   */
  case NotConfigured

  /**
   * The [[ServerSituation]] where the server is waiting for enough players
   * to join the chess game.
   */
  case WaitingForPlayers

  /**
   * The [[ServerSituation]] where the server is waiting for the chess game
   * to be started.
   */
  case Ready

  /**
   * The [[ServerSituation]] where the server is listening for actions to be
   * applied to the chess game.
   */
  case Running

  /**
   * The [[ServerSituation]] where the server has stopped due to the chess game
   * ending.
   */
  case Terminated
