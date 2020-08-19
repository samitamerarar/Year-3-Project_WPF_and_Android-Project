import {
  ITournamentGroup,
  IGroupPlayer,
  GameMode,
  ISocketUser,
  Socket,
  ITournamentRound,
} from "../../core/types";
import { GameChatSocketHandler } from "../../handlers/game.chat.socket.handler";
import { GameSocketHandler } from "../../handlers/game.socket.handler";
import { Events } from "../../core/events";
import { IClassicGameResults } from "../classic/classic.game";
import { TournamentRound } from "./tournament.round";
import { GroupUtils } from "../group.utils";
import { GameService } from "../../../services/game.server.service";
import { VirtualPlayer } from "../virtual.player";

const START_DELAY = 10000;

type Player = IGroupPlayer;

interface ITournamentState {
  state: IBracketStatus[][];
  startTimer?: number;
}

interface HashMap<V> {
  [key: string]: V;
}

interface IBracketStatus {
  username: string;
  status: "WAITING" | "PLAYING" | "WINNER" | "LOSER";
}

export class TournamentGame {
  private startTime: number;
  private endTime: number;
  public group: ITournamentGroup;
  public id: string;
  private gameHandler: GameSocketHandler;
  private brackets: ITournamentRound[][];
  private bracketStatuses: IBracketStatus[][];
  private currentGame: number;
  private participants: Player[];
  private gamesPlaying: HashMap<boolean>;
  private waiting: ISocketUser[];
  private gameChat: GameChatSocketHandler;
  private timeout: NodeJS.Timer;
  private forfeited: string[];
  private fWinners: string[];
  private end: () => void;
  private doSave: boolean = true;
  private virtualPlayer: VirtualPlayer;

  private get bracket() {
    return this.brackets[this.brackets.length - 1];
  }

  private get bracketStatus() {
    return this.bracketStatuses[this.bracketStatuses.length - 1];
  }

  public constructor(group: ITournamentGroup, gameHandler: GameSocketHandler) {
    this.startTime = Date.now();
    this.group = group;
    this.id = group.id;
    this.gameHandler = gameHandler;
    this.gameChat = gameHandler.gameChat;
    this.brackets = [];
    this.bracketStatuses = [];
    this.currentGame = 0;
    this.participants = [...this.group.players];
    this.gamesPlaying = {};
    this.waiting = [...this.group.players.map(p => p.user as ISocketUser)];
    this.forfeited = [];
    this.fWinners = [];
    this.virtualPlayer = new VirtualPlayer();
  }

  public async init(gameService: GameService) {
    const games = await gameService.getGames(this.group.difficulty);
    this.doSave = games.length > 0 ? true : false;
    this.matchMaking();
  }

  public async message(content: string) {
    await this.gameChat.newMessage(this.id, {
      author: this.virtualPlayer.name,
      channel: "game",
      content,
      created: new Date().toLocaleString(),
    });
  }

  public start(end: () => void) {
    this.end = end;

    if (!this.doSave) {
      this.finalize();
      return;
    }

    this.next();
  }

  private next() {
    this.waiting.forEach(p => this.emit(p.s, START_DELAY));
    this.timeout = setTimeout(() => this.startGames(), START_DELAY);
  }

  // tslint:disable-next-line:no-empty
  public forceFinish(s: Socket) {
    const user = this.waiting.find(u => u.s.id === s.id);
    if (!user) return;

    this.waiting = this.waiting.filter(u => u.s.id !== s.id);

    if (this.participants.find(p => (p.user as ISocketUser).s.id === s.id))
      this.forfeited.push(user.username);
  }

  private finish() {
    this.bracketStatuses.push([
      {
        username: (this.participants[0].user as ISocketUser).username,
        status: "WINNER",
      },
    ]);

    this.waiting.forEach(p => this.emit(p.s, START_DELAY));
    clearTimeout(this.timeout);
    setTimeout(() => this.finalize(), START_DELAY);
  }

  private async finalize() {
    const winner = (this.participants[0].user as ISocketUser).s.id;

    this.waiting.forEach(p =>
      p.s.emit(Events.GAME_FINISHED, {
        winner: p.s.id === winner && this.doSave,
        score: -1,
      })
    );

    this.endTime = Date.now();
    if (this.doSave) await this.save();
    this.end();
  }

  private matchMaking() {
    if (this.participants.length <= 1) {
      this.finish();
      return;
    }

    const bracket: ITournamentRound[] = [];
    const bracketStatuses: IBracketStatus[] = [];

    for (let i = 0; i < this.participants.length; i += 2) {
      bracket.push(
        this.createGroup(
          this.participants[i],
          this.participants[i + 1],
          this.currentGame++
        )
      );
    }

    this.participants.forEach(p => {
      bracketStatuses.push({
        username: (p.user as ISocketUser).username,
        status: "WAITING",
      });
    });

    this.bracketStatuses.push(bracketStatuses);
    this.brackets.push(bracket);
  }

  private createGroup(p1: Player, p2: Player, i: number): ITournamentRound {
    return {
      id: `${this.id}_game_${i}`,
      mode: GameMode.TOURNAMENT_ROUND,
      difficulty: this.group.difficulty,
      name: `${this.group.name}_game_${i}`,
      A: p1,
      B: p2,
    };
  }

  private async startGames() {
    let started = false;

    this.bracket.forEach(g => {
      if (!this.checkForfeit(g)) {
        started = true;
        this.startGame(g);
      }
    });

    this.bracketStatus.forEach(b => {
      if (this.fWinners.includes(b.username)) {
        b.status = "WINNER";
        this.fWinners = this.fWinners.filter(f => f !== b.username);
      } else if (this.forfeited.includes(b.username)) {
        b.status = "LOSER";
        this.forfeited = this.forfeited.filter(f => f !== b.username);
      } else {
        b.status = "PLAYING";
        this.waiting = this.waiting.filter(p => p.username !== b.username);
      }
    });

    this.waiting.forEach(p => this.emit(p.s));

    if (started) return;
    this.matchMaking();
    if (this.participants.length > 1) this.next();
  }

  private checkForfeit(g: ITournamentRound) {
    const usernames = [g.A, g.B].map(p => (p.user as ISocketUser).username);
    if (!usernames.some(u => this.forfeited.includes(u))) return false;

    const w = this.forfeited.includes(usernames[0])
      ? usernames[1]
      : usernames[0];

    const l = this.forfeited.includes(usernames[0])
      ? usernames[0]
      : usernames[1];

    this.fWinners.push(w);
    this.participants = this.participants.filter(
      p => (p.user as ISocketUser).username !== l
    );

    return true;
  }

  private async startGame(g: ITournamentRound) {
    this.gameChat.putChannel(g);
    const game = (await this.gameHandler.startGame(g)) as TournamentRound;
    this.gamesPlaying[game.id] = true;

    game.onFinish((gm: TournamentRound, f: boolean) => {
      this.gamesPlaying[gm.id] = false;
      this.refreshGames(this.filterResults(gm.results), f);
    });
  }

  private refreshGames(res: IClassicGameResults, f: boolean) {
    const losers = res.losers.map(l => (l.user as ISocketUser).username);
    const winners = res.winners.map(l => (l.user as ISocketUser).username);

    this.participants = this.participants.filter(
      p => !losers.includes((p.user as ISocketUser).username)
    );

    this.waiting = [
      ...this.waiting,
      ...res.winners.map(l => l.user as ISocketUser),
      ...(f ? [] : res.losers.map(l => l.user as ISocketUser)),
    ];

    this.bracketStatus.forEach((b, i, a) => {
      if (losers.includes(b.username)) a[i].status = "LOSER";
      if (winners.includes(b.username)) a[i].status = "WINNER";
    });

    this.waiting.forEach(p => this.emit(p.s));

    if (Object.values(this.gamesPlaying).some(v => v)) return;

    this.matchMaking();
    if (this.participants.length > 1) this.next();
  }

  private filterResults(res: IClassicGameResults) {
    const losers = res.losers.filter(l => l.type === "REAL");
    const winners = res.winners.filter(w => w.type === "REAL");
    return { winners, losers };
  }

  private emit(s: Socket, startTimer?: number) {
    console.log("TOURNAMENT STATE");
    s.emit<ITournamentState>(Events.TOURNAMENT_STATE, {
      state: this.bracketStatuses,
      startTimer: startTimer,
    });
  }

  protected async save() {
    const usernames = GroupUtils.use(this.group).usernames;
    const w = [(this.participants[0].user as ISocketUser).username];
    const l = usernames.filter(u => u !== w[0]);
    const resultClassic = { w, l };

    const match = {
      start: this.startTime,
      end: this.endTime,
      duration: this.endTime - this.startTime,
      participants: usernames,
      mode: this.group.mode,
      resultClassic,
      resultCoop: undefined,
    };

    await new GameService().saveMatch(match);
  }
}
