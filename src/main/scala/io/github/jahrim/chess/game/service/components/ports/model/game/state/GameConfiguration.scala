package io.github.jahrim.chess.game.service.components.ports.model.game.state

import io.github.chess.engine.model.configuration.{
  BlackPlayer as LegacyBlackPlayer,
  GameConfiguration as LegacyGameConfiguration,
  GameMode as LegacyGameMode,
  Player as LegacyPlayer,
  TimeConstraint as LegacyTimeConstraint,
  WhitePlayer as LegacyWhitePlayer
}
import io.github.chess.engine.model.game.Team as LegacyTeam
import io.github.jahrim.chess.game.service.components.ports.model.game.state.GameConfiguration.*
import io.github.jahrim.chess.game.service.components.ports.model.game.state.ServerState.Id

/** Adapter of a [[LegacyGameConfiguration LegacyGameConfiguration]]. */
trait GameConfiguration:
  /** The [[LegacyGameConfiguration LegacyGameConfiguration]] adapted by this [[GameConfiguration]]. */
  def legacy: LegacyGameConfiguration

  /** The [[LegacyGameMode LegacyGameMode]] of this chess game. */
  def gameMode: LegacyGameMode = legacy.gameMode

  /** The [[LegacyTimeConstraint LegacyTimeConstraint]] of this chess game. */
  def timeConstraint: LegacyTimeConstraint = legacy.timeConstraint

  /**
   * @param team the specified [[LegacyTeam LegacyTeam]].
   * @return a [[Some]] containing the [[LegacyPlayer LegacyPlayer]] of the
   *         specified [[LegacyTeam LegacyTeam]] who joined this chess game
   *         if set; a [[None]] otherwise.
   */
  def playerOption(team: LegacyTeam): Option[LegacyPlayer] =
    if team == LegacyTeam.WHITE then whitePlayerOption else blackPlayerOption

  /** As [[playerOption playerOption(LegacyTeam.WHITE)]]. */
  def whitePlayerOption: Option[LegacyWhitePlayer] =
    Some(legacy.whitePlayer).filterNot(_ == NoWhitePlayer)

  /** As [[playerOption playerOption(LegacyTeam.BLACK)]]. */
  def blackPlayerOption: Option[LegacyBlackPlayer] =
    Some(legacy.blackPlayer).filterNot(_ == NoBlackPlayer)

  /** @return true if this chess game is private, false otherwise. */
  def isPrivate: Boolean

  /** @return true if this chess game is public, false otherwise. */
  def isPublic: Boolean = !this.isPrivate

  /**
   * @return a [[Some]] containing the [[Id Id]] of this chess game if set;
   *         a [[None]] otherwise.
   */
  def gameIdOption: Option[Id]

  /**
   * @param timeConstraint the specified [[LegacyTimeConstraint LegacyTimeConstraint]].
   * @return a new [[GameConfiguration]] obtained by updating the [[LegacyTimeConstraint LegacyTimeConstraint]]
   *         of this [[GameConfiguration]] to the specified [[LegacyTimeConstraint LegacyTimeConstraint]].
   */
  def setTimeConstraint(timeConstraint: LegacyTimeConstraint): GameConfiguration

  /**
   * @param gameMode the specified [[LegacyGameMode LegacyGameMode]].
   * @return a new [[GameConfiguration]] obtained by updating the [[LegacyGameMode LegacyGameMode]]
   *         of this [[GameConfiguration]] to the specified [[LegacyGameMode LegacyGameMode]].
   */
  def setGameMode(gameMode: LegacyGameMode): GameConfiguration

  /**
   * @param whitePlayer the specified [[LegacyWhitePlayer LegacyWhitePlayer]].
   * @return a new [[GameConfiguration]] obtained by updating the [[LegacyWhitePlayer LegacyWhitePlayer]]
   *         of this [[GameConfiguration]] to the specified [[LegacyWhitePlayer LegacyWhitePlayer]].
   */
  def setWhitePlayer(whitePlayer: LegacyWhitePlayer): GameConfiguration

  /**
   * @param blackPlayer the specified [[LegacyBlackPlayer LegacyBlackPlayer]].
   * @return a new [[GameConfiguration]] obtained by updating the [[LegacyBlackPlayer LegacyBlackPlayer]]
   *         of this [[GameConfiguration]] to the specified [[LegacyBlackPlayer LegacyBlackPlayer]].
   */
  def setBlackPlayer(blackPlayer: LegacyBlackPlayer): GameConfiguration

  /**
   * @param gameId the specified [[Id Id]].
   * @return a new [[GameConfiguration]] obtained by updating the game [[Id Id]]
   *         of this [[GameConfiguration]] to the specified [[Id Id]].
   */
  def setGameId(gameId: Id): GameConfiguration

  /**
   * @param isPrivate the specified privacy settings.
   * @return a new [[GameConfiguration]] obtained by updating the privacy settings
   *         of this [[GameConfiguration]] to the specified privacy settings.
   */
  def setIsPrivate(isPrivate: Boolean): GameConfiguration

/** Companion object of [[GameConfiguration]]. */
object GameConfiguration:
  /**
   * The value of a missing [[LegacyWhitePlayer LegacyWhitePlayer]] in a
   * [[GameConfiguration]].
   */
  private val NoWhitePlayer: LegacyWhitePlayer = LegacyWhitePlayer("")

  /**
   * The value of a missing [[LegacyBlackPlayer LegacyBlackPlayer]] in a
   * [[GameConfiguration]].
   */
  private val NoBlackPlayer: LegacyBlackPlayer = LegacyBlackPlayer("")

  /** The value of a missing game [[Id Id]] in a [[GameConfiguration]]. */
  private val NoGameId: String = ""

  /** The default configuration for the time mode of a chess game. */
  val DefaultTimeConstraint: LegacyTimeConstraint = LegacyTimeConstraint.NoLimit

  /** The default configuration for the game mode of a chess game. */
  val DefaultGameMode: LegacyGameMode = LegacyGameMode.PVP

  /** The default configuration for the id of a chess game. */
  val DefaultGameId: String = NoGameId

  /** The default configuration for the privacy of a chess game. */
  val DefaultIsPrivate: Boolean = false

  /** The default configuration for the white player of a chess game. */
  val DefaultWhitePlayer: LegacyWhitePlayer = NoWhitePlayer

  /** The default configuration for the black player of a chess game. */
  val DefaultBlackPlayer: LegacyBlackPlayer = NoBlackPlayer

  /** The default [[GameConfiguration]] for a chess game. */
  private val DefaultGameConfiguration: GameConfiguration = GameConfiguration()

  /** @return the default [[GameConfiguration]] for a chess game. */
  def default: GameConfiguration = DefaultGameConfiguration

  /**
   * Create a new [[GameConfiguration]].
   *
   * @param timeConstraint the [[LegacyTimeConstraint LegacyTimeConstraint]] of the new [[GameConfiguration]].
   * @param gameMode       the [[LegacyGameMode LegacyGameMode]] of the new [[GameConfiguration]].
   * @param whitePlayer    the [[LegacyWhitePlayer LegacyWhitePlayer]] of the new [[GameConfiguration]].
   * @param blackPlayer    the [[LegacyBlackPlayer LegacyBlackPlayer]] of the new [[GameConfiguration]].
   * @param gameId         the identifier of the chess game configured by this [[GameConfiguration]].
   * @param isPrivate      true if this [[GameConfiguration]] belongs to a private chess game,
   *                       false otherwise.
   * @return a new [[GameConfiguration]].
   */
  def apply(
      timeConstraint: LegacyTimeConstraint = DefaultTimeConstraint,
      gameMode: LegacyGameMode = DefaultGameMode,
      whitePlayer: LegacyWhitePlayer = DefaultWhitePlayer,
      blackPlayer: LegacyBlackPlayer = DefaultBlackPlayer,
      gameId: Id = DefaultGameId,
      isPrivate: Boolean = DefaultIsPrivate
  ): GameConfiguration =
    BasicGameConfiguration(
      LegacyGameConfiguration(timeConstraint, gameMode, whitePlayer, blackPlayer),
      gameId,
      isPrivate
    )

  /** Basic implementation of [[GameConfiguration]]. */
  private case class BasicGameConfiguration(
      override val legacy: LegacyGameConfiguration,
      private val gameId: Id,
      override val isPrivate: Boolean
  ) extends GameConfiguration:
    override def gameIdOption: Option[Id] = Some(gameId).filterNot(_ == NoGameId)
    override def setTimeConstraint(
        timeConstraint: LegacyTimeConstraint
    ): BasicGameConfiguration =
      this.update(timeConstraint = timeConstraint)
    override def setGameMode(gameMode: LegacyGameMode): GameConfiguration =
      this.update(gameMode = gameMode)
    override def setWhitePlayer(whitePlayer: LegacyWhitePlayer): GameConfiguration =
      this.update(whitePlayer = whitePlayer)
    override def setBlackPlayer(blackPlayer: LegacyBlackPlayer): GameConfiguration =
      this.update(blackPlayer = blackPlayer)
    override def setGameId(gameId: Id): BasicGameConfiguration =
      this.update(gameId = gameId)
    override def setIsPrivate(isPrivate: Boolean): BasicGameConfiguration =
      this.update(isPrivate = isPrivate)

    /**
     * @param timeConstraint the [[LegacyTimeConstraint LegacyTimeConstraint]] of the new [[GameConfiguration]].
     * @param gameMode       the [[LegacyGameMode LegacyGameMode]] of the new [[GameConfiguration]].
     * @param whitePlayer    the [[LegacyWhitePlayer LegacyWhitePlayer]] of the new [[GameConfiguration]].
     * @param blackPlayer    the [[LegacyBlackPlayer LegacyBlackPlayer]] of the new [[GameConfiguration]].
     * @param gameId         the identifier of the chess game configured by this [[GameConfiguration]].
     * @param isPrivate      true if this [[GameConfiguration]] belongs to a private chess game,
     *                       false otherwise.
     * @return a new [[BasicGameConfiguration]] obtained by updating this [[BasicGameConfiguration]] with the
     *         specified configurations.
     */
    private def update(
        timeConstraint: LegacyTimeConstraint = this.timeConstraint,
        gameMode: LegacyGameMode = this.gameMode,
        whitePlayer: LegacyWhitePlayer = this.whitePlayerOption.getOrElse(NoWhitePlayer),
        blackPlayer: LegacyBlackPlayer = this.blackPlayerOption.getOrElse(NoBlackPlayer),
        gameId: Id = this.gameIdOption.getOrElse(GameConfiguration.NoGameId),
        isPrivate: Boolean = this.isPrivate
    ): BasicGameConfiguration =
      BasicGameConfiguration(
        LegacyGameConfiguration(timeConstraint, gameMode, whitePlayer, blackPlayer),
        gameId,
        isPrivate
      )
