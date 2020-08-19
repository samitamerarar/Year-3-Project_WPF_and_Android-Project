import { IGame, GameDifficulty } from "../../models/game.model";
import {
  IGroupPlayer,
  ISocketUser,
  IDrawPoint,
  IDraw,
  IGroup,
  GameMode,
  Socket,
} from "../core/types";
import { Utils } from "../../utils/utils";
import { GameService } from "../../services/game.server.service";
import { VirtualPlayer } from "./virtual.player";
import { Events } from "../core/events";
import { GameChatSocketHandler } from "../handlers/game.chat.socket.handler";
import { GroupUtils, UseGroupUtils } from "./group.utils";

export abstract class BaseGame<T extends IGroup> {
  public id: string;
  public finished: boolean;

  public group: T;
  protected rounds: IGame[];
  protected currentPlay: number;
  protected games: IGame[];
  protected virtualPlayer: VirtualPlayer;
  protected gameChat: GameChatSocketHandler;
  protected timeout: NodeJS.Timer;
  protected end: () => void;
  protected utils: UseGroupUtils;
  protected startTime: number;
  protected endTime: number;
  protected attempts: number;
  protected maxAttempts: number;
  protected finishEvents: ((args: any, f: boolean) => void)[];

  protected get round() {
    return this.rounds[this.rounds.length - 1];
  }

  protected get clue() {
    return Utils.any(this.round.clues);
  }

  protected get sockets() {
    return this.utils.sockets;
  }

  protected get players() {
    return this.utils.players;
  }

  public constructor(group: T, gameChat: GameChatSocketHandler) {
    this.id = group.id;
    this.group = group;
    this.gameChat = gameChat;
    this.rounds = [];
    this.currentPlay = -1;
    this.finished = false;
    this.virtualPlayer = new VirtualPlayer();
    this.utils = GroupUtils.use(this.group);
    this.startTime = Date.now();
    this.attempts = 0;
    this.finishEvents = [];
    this.maxAttempts =
      this.group.difficulty === GameDifficulty.EASY
        ? 15
        : this.group.difficulty === GameDifficulty.INTERMEDIATE
        ? 10
        : 5;
  }

  public async init(gameService: GameService) {
    this.games = await gameService.getGames(this.group.difficulty);
    this.initHandlers();
  }

  public start(end: () => void) {
    this.end = end;

    if (this.games.length < 1) {
      this.done(true);
    }

    this.nextIteration();
  }

  public forceFinish(s: Socket) {
    this.done(true, s);
  }

  public onFinish<A extends IGroup>(
    cb: (args: BaseGame<A>, f: boolean) => void
  ) {
    this.finishEvents.push(cb);
  }

  protected nextIteration() {
    if (this.finished) {
      this.done();
      return;
    }

    const interval = this.next();
    this.timeout = setTimeout(() => this.nextIteration(), interval);
  }

  protected async done(force = false, s?: Socket) {
    clearTimeout(this.timeout);
    this.virtualPlayer.stop();
    this.endTime = Date.now();
    this.finish(force, s);
    this.finishEvents.forEach(ev => ev(this, force));
    if (!force) await this.save();
    this.end();
  }

  protected abstract initHandlers(): void;
  public abstract next(): number;
  public abstract finish(force: boolean, s?: Socket): void;

  protected initHandler(player: IGroupPlayer) {
    if (!player || player.type !== "REAL") return;

    const s = (player.user as ISocketUser).s;

    s.on(Events.DRAW, (point: IDrawPoint) => this.draw(point));
    s.on(Events.CLUE, () => this.sendClue());
    s.on(Events.GUESS, (guess: string) =>
      this.guess(guess, this.utils.username(s))
    );
  }

  protected leaveHandler(player: IGroupPlayer) {
    if (!player || player.type !== "REAL") return;

    const s = (player.user as ISocketUser).s;
    s.forget(Events.DRAW);
    s.forget(Events.CLUE);
    s.forget(Events.GUESS);
  }

  protected sendClue() {
    this.message(`Indice: ${this.clue}`);
  }

  public async message(content: string) {
    await this.gameChat.newMessage(this.id, {
      author: this.virtualPlayer.name,
      channel: "game",
      content,
      created: new Date().toLocaleString(),
    });
  }

  protected async save() {
    const resultClassic = [
      GameMode.CLASSIC,
      GameMode.TOURNAMENT_ROUND,
    ].includes(this.group.mode)
      ? (this.res as {
          w: string[];
          l: string[];
        })
      : undefined;

    const resultCoop = [GameMode.SOLO, GameMode.COOP].includes(this.group.mode)
      ? (this.res as number)
      : undefined;

    const match = {
      start: this.startTime,
      end: this.endTime,
      duration: this.endTime - this.startTime,
      participants: this.utils.usernames,
      mode: this.group.mode,
      resultClassic,
      resultCoop,
    };

    await new GameService().saveMatch(match);
  }

  // tslint:disable-next-line:no-empty
  protected draw(point: IDraw): void {}
  protected abstract guess(guess: string, username: string): void;
  protected abstract get res(): number | { w: string[]; l: string[] };
}
