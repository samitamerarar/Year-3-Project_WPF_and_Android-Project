import {
  ICoopGroup,
  IGroupPlayer,
  ISocketUser,
  IGameState,
  ISoloGroup,
} from "../../core/types";
import { Events } from "../../core/events";
import { Utils } from "../../../utils/utils";
import { BaseGame } from "../base.game";
import { GameChatSocketHandler } from "../../handlers/game.chat.socket.handler";
import { GameDifficulty } from "../../../models/game.model";
import { VirtualPersonality } from "../virtual.personality";

const INTERVAL_TIME = 60000;

const EASY_BONUS_TIME = 15000;
const MEDIUM_BONUS_TIME = 10000;
const HARD_BONUS_TIME = 5000;

type CoopGroup = ICoopGroup | ISoloGroup;

export class CoopGame extends BaseGame<CoopGroup> {
  private totalInterval: number;
  private bonus: number;

  protected get res() {
    return this.group.A.points;
  }

  public constructor(group: CoopGroup, gameChat: GameChatSocketHandler) {
    super(group, gameChat);
    this.totalInterval = INTERVAL_TIME;
    this.bonus =
      this.group.difficulty === GameDifficulty.EASY
        ? EASY_BONUS_TIME
        : this.group.difficulty === GameDifficulty.INTERMEDIATE
        ? MEDIUM_BONUS_TIME
        : HARD_BONUS_TIME;
  }

  // Only one major round in this mode so the finished flag is set to true immediately
  public next(): number {
    this.finished = true;
    this.startTime = Date.now();
    this.nextRound();
    return INTERVAL_TIME;
  }

  public finish(_force: boolean) {
    this.utils.players.forEach(p => this.winner(p));
  }

  protected initHandlers() {
    this.utils.players.forEach(p => this.initHandler(p));
  }

  private winner(player: IGroupPlayer) {
    if (player.type !== "REAL") return;

    console.log("GAME FINISHED COOP");
    (player.user as ISocketUser).s.emit(Events.GAME_FINISHED, {
      winner: true,
      score: this.group.A.points,
    });

    this.leaveHandler(player);
  }

  private nextRound() {
    this.message("Nouvelle Manche!");
    this.attempts = 0;
    this.rounds.push(Utils.any(this.games));
    const remainingTime = Math.abs(
      this.totalInterval - (Date.now() - this.startTime)
    );
    this.emitGroup(true, remainingTime);
    this.virtualPlayer.startGame(this.round, this.sockets);
  }

  protected guess(guess: string, username: string) {
    this.attempts++;

    let scored = false;
    if (this.round.answer === guess) {
      this.group.A.points++;
      scored = true;
    }

    const bonus = scored ? this.bonus : 0;

    if (scored || this.attempts >= this.maxAttempts) {
      this.message(VirtualPersonality.congratulate(username, guess));
      this.virtualPlayer.stop();
      this.pushTimer(bonus);
      this.nextRound();
    } else {
      this.message(VirtualPersonality.encourage(username, guess));
      this.emitGroup();
    }
  }

  private pushTimer(bonus: number) {
    this.totalInterval += bonus;
    const remainingTime = Math.abs(
      this.totalInterval - (Date.now() - this.startTime)
    );
    clearTimeout(this.timeout);
    this.timeout = setTimeout(() => this.nextIteration(), remainingTime);
  }

  private emitGroup(clr: boolean = false, t?: number) {
    this.emitState(this.group.A.A, clr, t);
    this.emitState(this.group.A.B, clr, t);
    this.emitState(this.group.A.C, clr, t);
    this.emitState(this.group.A.D, clr, t);
  }

  private emitState(player: IGroupPlayer, clear: boolean, startTimer?: number) {
    if (!player || player.type !== "REAL") return;

    const role = "GUESS";
    const points = this.group.A.points;
    const id = this.id;
    const currentRound = this.rounds.length - 1;

    const state: IGameState = {
      role,
      points,
      otherPoints: -1,
      id,
      clear,
      currentRound,
      counter: false,
      startTimer,
      attemptsLeft: this.maxAttempts - this.attempts,
    };

    console.log("STATE UPDATE: COOP");
    (player.user as ISocketUser).s.emit(Events.STATE_UPDATE, state);
  }
}
