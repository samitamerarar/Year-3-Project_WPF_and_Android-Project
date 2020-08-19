import {
  IGroupPlayer,
  ISocketUser,
  IGameState,
  ITournamentRound,
  Socket,
} from "../../core/types";
import { Events } from "../../core/events";
import { Utils } from "../../../utils/utils";
import { BaseGame } from "../base.game";
import { GameChatSocketHandler } from "../../handlers/game.chat.socket.handler";
import { VirtualPersonality } from "../virtual.personality";
import { GroupUtils } from "../group.utils";

const INTERVAL_TIME = 60000;
const MAX_POINTS = 2;

export class TournamentRound extends BaseGame<ITournamentRound> {
  private pointsA: number;
  private pointsB: number;
  private isAWinner: boolean;

  public get results() {
    const winners = this.isAWinner ? [this.group.A] : [this.group.B];
    const losers = this.isAWinner ? [this.group.B] : [this.group.A];
    return { winners, losers };
  }

  protected get res() {
    const a = (this.group.A.user as ISocketUser).username;
    const b = (this.group.B.user as ISocketUser).username;
    const w = this.pointsA > this.pointsB ? [a] : [b];
    const l = this.pointsA > this.pointsB ? [b] : [a];

    return { w, l };
  }

  public constructor(group: ITournamentRound, gameChat: GameChatSocketHandler) {
    super(group, gameChat);
    this.pointsA = 0;
    this.pointsB = 0;
    this.isAWinner = false;
  }

  public next(): number {
    this.nextRound();
    return INTERVAL_TIME;
  }

  public finish(force: boolean, s?: Socket) {
    this.isAWinner = force
      ? this.forceFinishWinner(s)
      : this.pointsA > this.pointsB;

    const f = !force || s !== undefined;
    this.winner(this.isAWinner && f, this.group.A);
    this.winner(!this.isAWinner && f, this.group.B);
  }

  private forceFinishWinner(s?: Socket) {
    if (!s) return false;
    return GroupUtils.sids([this.group.B]).includes(s.id);
  }

  private winner(winner: boolean, player: IGroupPlayer) {
    if (player.type !== "REAL") return;

    console.log("EMIT ROUND FINISHED");

    (player.user as ISocketUser).s.emit(Events.ROUND_FINISHED, {
      winner,
      score: -1,
    });

    this.leaveHandler(player);
  }

  protected initHandlers() {
    this.utils.players.forEach(p => this.initHandler(p));
  }

  private nextRound() {
    this.message("Nouvelle Manche!");
    this.attempts = 0;
    this.rounds.push(Utils.any(this.games));
    this.emitGroup(true, INTERVAL_TIME);
    this.virtualPlayer.startGame(this.round, this.sockets);
  }

  protected guess(guess: string, username: string) {
    console.log("TOURNAMENT ROUND");
    let scored = false;

    if (this.round.answer === guess) {
      if (username === (this.group.A.user as ISocketUser).username) {
        this.pointsA++;
      } else {
        this.pointsB++;
      }
      scored = true;
    }

    this.finished = this.pointsA >= MAX_POINTS || this.pointsB >= MAX_POINTS;

    if (scored) {
      this.message(VirtualPersonality.congratulate(username, guess));
      clearTimeout(this.timeout);
      this.virtualPlayer.stop();
      this.nextIteration();
    } else {
      this.message(VirtualPersonality.encourage(username, guess));
      this.emitGroup();
    }
  }

  private emitGroup(clr: boolean = false, t?: number) {
    this.emitState(this.group.A, this.pointsA, this.pointsB, clr, t);
    this.emitState(this.group.B, this.pointsB, this.pointsA, clr, t);
  }

  private emitState(
    player: IGroupPlayer,
    points: number,
    otherPoints: number,
    clear: boolean,
    startTimer?: number
  ) {
    if (!player || player.type !== "REAL") return;

    const role = "GUESS";
    const id = this.id;
    const currentRound = this.rounds.length - 1;

    const state: IGameState = {
      role,
      points,
      otherPoints,
      id,
      clear,
      currentRound,
      counter: false,
      startTimer,
      attemptsLeft: -1,
    };

    console.log("TOURNAMENT ROUND STATE");
    (player.user as ISocketUser).s.emit(Events.STATE_UPDATE, state);
  }
}
