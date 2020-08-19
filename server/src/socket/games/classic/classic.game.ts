import {
  IClassicGroup,
  IGroupPlayer,
  ISocketUser,
  IGameState,
  IClassicGroupTeam,
  IDraw,
  Socket,
} from "../../core/types";
import { Events } from "../../core/events";
import { Utils } from "../../../utils/utils";
import { ClassicPlay as Play } from "./classic.play";
import { BaseGame } from "../base.game";
import { GameChatSocketHandler } from "../../handlers/game.chat.socket.handler";
import { VirtualPersonality } from "../virtual.personality";
import { GroupUtils } from "../group.utils";

const MAX_POINTS = 5;
const COUNTER_INTERVAL = 15000;
const INTERVAL_TIME = 60000;

const PLAYS = [
  new Play("A", "B"),
  new Play("A", "A"),
  new Play("B", "B"),
  new Play("B", "A"),
];

export interface IClassicGameResults {
  winners?: IGroupPlayer[];
  losers?: IGroupPlayer[];
}

export class ClassicGame extends BaseGame<IClassicGroup> {
  private scored: boolean;
  private _counter: boolean;

  public get results(): IClassicGameResults {
    const winTeam =
      this.group.A.points > this.group.B.points ? this.group.A : this.group.B;
    const loseTeam =
      this.group.A.points > this.group.B.points ? this.group.B : this.group.A;

    const winners = [winTeam.A, winTeam.B];
    const losers = [loseTeam.A, loseTeam.B];

    return { winners, losers };
  }

  protected get res() {
    const results = this.results;

    const w = results.winners
      .filter(p => p.type === "REAL")
      .map(p => (p.user as ISocketUser).username);

    const l = results.losers
      .filter(p => p.type === "REAL")
      .map(p => (p.user as ISocketUser).username);

    return { w, l };
  }

  private get virtual() {
    const AB =
      this.group.A.B.type === "VIRTUAL" && this.group.A.B.role === "WRITE";
    const BB =
      this.group.B.B.type === "VIRTUAL" && this.group.B.B.role === "WRITE";

    return AB || BB;
  }

  public constructor(group: IClassicGroup, gameChat: GameChatSocketHandler) {
    super(group, gameChat);
    this.scored = true;
    this._counter = false;
  }

  public next(): number {
    this.scored ? this.nextRound() : this.counter();
    if (this.virtual) this.virtualPlayer.startGame(this.round, this.sockets);

    return this.scored ? COUNTER_INTERVAL : INTERVAL_TIME;
  }

  public finish(force: boolean, s?: Socket) {
    const isTeamAWinner = force
      ? this.forceFinishWinner(s)
      : this.group.A.points > this.group.B.points;

    const f = !force || s !== undefined;
    this.winner(isTeamAWinner && f, this.group.A.A);
    this.winner(isTeamAWinner && f, this.group.A.B);
    this.winner(!isTeamAWinner && f, this.group.B.A);
    this.winner(!isTeamAWinner && f, this.group.B.B);
  }

  private forceFinishWinner(s?: Socket) {
    if (!s) return false;
    return GroupUtils.sids([this.group.B.A, this.group.B.B]).includes(s.id);
  }

  protected initHandlers() {
    this.initHandler(this.group.A.A);
    this.initHandler(this.group.A.B);
    this.initHandler(this.group.B.A);
    this.initHandler(this.group.B.B);
  }

  private nextRound() {
    this.message("Nouvelle Manche!");
    this.scored = false;
    this._counter = false;
    this.attempts = 0;
    this.currentPlay = (this.currentPlay + 1) % 4;
    this.rounds.push(Utils.any(this.games));
    this.group.A.role = PLAYS[this.currentPlay].A;
    this.group.B.role = PLAYS[this.currentPlay].B;
    this.group.A.A!.role = PLAYS[this.currentPlay].role("AA", this.group);
    this.group.A.B!.role = PLAYS[this.currentPlay].role("AB", this.group);
    this.group.B.A!.role = PLAYS[this.currentPlay].role("BA", this.group);
    this.group.B.B!.role = PLAYS[this.currentPlay].role("BB", this.group);
    this.emitGroup(true, INTERVAL_TIME);
  }

  private counter() {
    this.attempts = 0;
    this.group.A.role = this.group.A.role === "ACTIVE" ? "PASSIVE" : "ACTIVE";
    this.group.B.role = this.group.B.role === "ACTIVE" ? "PASSIVE" : "ACTIVE";

    this.group.A.A!.role = this.group.A.B!.role =
      this.group.A.A!.role === "PASSIVE" ? "GUESS" : "PASSIVE";

    this.group.B.A!.role = this.group.B.B!.role =
      this.group.B.A!.role === "PASSIVE" ? "GUESS" : "PASSIVE";

    this.emitGroup(false, COUNTER_INTERVAL, true);
    this.scored = true;
    this._counter = true;
  }

  protected guess(guess: string, username: string) {
    if (this.scored && !this._counter) return;

    this.attempts++;
    let point = 0;

    if (guess === this.round.answer) {
      point++;
      this.scored = true;
      if (this._counter) this._counter = false;
    }

    if (this.group.A.role === "ACTIVE") {
      this.group.A.points += point;
    } else {
      this.group.B.points += point;
    }

    this.finished =
      this.group.A.points >= MAX_POINTS || this.group.B.points >= MAX_POINTS;

    if (this.scored || this.attempts >= this.maxAttempts) {
      this.message(VirtualPersonality.congratulate(username, guess));
      clearTimeout(this.timeout);
      this.virtualPlayer.stop();
      this.nextIteration();
    } else {
      this.message(VirtualPersonality.encourage(username, guess));
      this.emitGroup();
    }
  }

  protected draw(point: IDraw) {
    this.emitPoint(this.group.A.A, point);
    this.emitPoint(this.group.A.B, point);
    this.emitPoint(this.group.B.A, point);
    this.emitPoint(this.group.B.B, point);
  }

  private winner(winner: boolean, player: IGroupPlayer) {
    if (player.type !== "REAL") return;

    (player.user as ISocketUser).s.emit(Events.GAME_FINISHED, {
      winner,
      score: -1,
    });

    this.leaveHandler(player);
  }

  private emitGroup(
    clr: boolean = false,
    t?: number,
    counter: boolean = false
  ) {
    this.emitState(this.group.A, this.group.B, this.group.A.A, clr, counter, t);
    this.emitState(this.group.A, this.group.B, this.group.A.B, clr, counter, t);
    this.emitState(this.group.B, this.group.A, this.group.B.A, clr, counter, t);
    this.emitState(this.group.B, this.group.A, this.group.B.B, clr, counter, t);
  }

  private emitPoint(player: IGroupPlayer, point: IDraw) {
    if (player.type === "REAL" && player.role !== "WRITE") {
      (player.user as ISocketUser).s.emit(Events.DRAW, [point]);
    }
  }

  private emitState(
    team: IClassicGroupTeam,
    otherTeam: IClassicGroupTeam,
    player: IGroupPlayer,
    clear: boolean,
    counter: boolean,
    startTimer?: number
  ) {
    if (player.type !== "REAL") return;

    const role = player.role;
    const points = team.points;
    const otherPoints = otherTeam.points;
    const answer = role === "WRITE" ? this.round.answer : undefined;
    const id = this.id;
    const currentRound = this.rounds.length - 1;
    const attemptsLeft = counter ? 1 : this.maxAttempts - this.attempts;

    const state: IGameState = {
      role,
      points,
      otherPoints,
      answer,
      id,
      clear,
      currentRound,
      counter,
      startTimer,
      attemptsLeft,
    };

    console.log("STATE UPDATE: CLASSIC");
    (player.user as ISocketUser).s.emit(Events.STATE_UPDATE, state);
  }
}
