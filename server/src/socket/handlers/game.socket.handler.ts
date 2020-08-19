import { IGroup, GameMode, Socket } from "../core/types";
import { ClassicGame } from "../games/classic/classic.game";
import { GameService } from "../../services/game.server.service";
import { GameChatSocketHandler } from "./game.chat.socket.handler";
import { CoopGame } from "../games/coop/coop.game";
import { GroupUtils } from "../games/group.utils";
import { TournamentGame } from "../games/tournament/tournament.game";
import { TournamentRound } from "../games/tournament/tournament.round";

type Game = CoopGame | ClassicGame | TournamentGame | TournamentRound;

interface IGames {
  [key: string]: Game;
}

export class GameSocketHandler {
  private gameService: GameService;
  private games: IGames;
  public gameChat: GameChatSocketHandler;

  public constructor(gameChat: GameChatSocketHandler) {
    this.gameChat = gameChat;
    this.gameService = new GameService();
    this.games = {};
  }

  public disconnect(s: Socket) {
    const keys = Object.keys(this.games).filter(id =>
      GroupUtils.use(this.games[id].group).sockets.some(
        sock => sock.id === s.id
      )
    );

    console.log("DISCONNECTED SOCKET IN GAME => " + keys);
    keys.forEach(k => this.games[k].forceFinish(s));
  }

  public async startGame(group: IGroup) {
    let game: Game;

    switch (group.mode) {
      case GameMode.CLASSIC:
        game = new ClassicGame(group, this.gameChat);
        break;

      case GameMode.COOP:
      case GameMode.SOLO:
        game = new CoopGame(group, this.gameChat);
        break;

      case GameMode.TOURNAMENT:
        game = new TournamentGame(group, this);
        break;

      case GameMode.TOURNAMENT_ROUND:
        game = new TournamentRound(group, this.gameChat);
        break;

      default:
        return;
    }

    await this.initialize(game);

    return game;
  }

  private async initialize(game: Game) {
    await game.init(this.gameService);

    const id = game.id;
    this.games[id] = game;
    await this.games[id].message("La partie à commencer! Bonne chance à tous!");

    const end = () => this.finish(id);
    this.games[id].start(end);
  }

  private finish(id: string) {
    delete this.games[id];
    this.gameChat.deleteChannel(id);
  }
}
