package io.github.jahrim.chess.game.service.components.ports
import io.github.chess.engine.events.Event
import io.github.chess.engine.model.pieces.Piece
import io.github.jahrim.chess.game.service.components.data.{GameConfigurationData, MoveData, PieceData, PlayerData, PositionData, PromoteToData}
import io.vertx.core.Future

import scala.reflect.ClassTag

class ChessGameModel extends ChessGamePort:
  override def createGame(gameConfiguration: GameConfigurationData): Future[String] = ???

  override def findPublicGame(): Future[String] = ???

  override def findPrivateGame(gameId: String): Future[String] = ???

  override def joinGame(gameId: String, player: PlayerData): Future[Unit] = ???

  override def findMoves(gameId: String, position: PositionData): Future[Set[MoveData]] = ???

  override def applyMove(gameId: String, move: MoveData): Future[Unit] = ???

  override def promote[P <: Piece](gameId: String, pawn: PieceData, to: PromoteToData): Future[P] = ???

  override def subscribe[T <: Event : ClassTag](handler: T => Unit): Future[String] = ???

  override def unsubscribe(subscriptionIds: String*): Future[Unit] = ???
