import { ServiceStatus } from "./core/interfaces";
import {
  IGame,
  Game,
  IGameDocument,
  GameDifficulty,
} from "../models/game.model";
import { IMatch, Match } from "../models/match.model";

type GameCreationStatus = ServiceStatus<null>;

export class GameService {
  public async createGame(form: IGame): Promise<GameCreationStatus> {
    if ((await this.getGame(form.name))[0]) {
      return { code: 409, msg: "Name already used!", data: null };
    }

    const game = new Game(form);
    game.save();

    return { code: 200, msg: "Game created!", data: null };
  }

  public async getGames(difficulty: GameDifficulty) {
    return await Game.find({ difficulty });
  }

  public async getGame(name: string): Promise<[boolean, IGameDocument?]> {
    let game: IGameDocument;

    try {
      game = await Game.findOne({ name }).exec();
      if (game === null) throw new Error("");
    } catch (e) {
      return [false, undefined];
    }

    return [true, game];
  }

  public async saveMatch(m: IMatch) {
    await new Match(m).save();
  }

  public async getMatches(username: string): Promise<IMatch[]> {
    return Match.find({ participants: username }).exec();
  }
}
