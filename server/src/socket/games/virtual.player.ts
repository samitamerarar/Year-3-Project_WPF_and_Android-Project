import { IGame, GameDifficulty, DrawingMode } from "../../models/game.model";
import { SvgParser, ISvgCommand } from "../../utils/svg.parser.utils";
import { Socket, IDrawPoint } from "../core/types";
import { Events } from "../core/events";
import { ImageParser } from "../../utils/image.parser.utils";
import { Utils } from "../../utils/utils";

type Command = ISvgCommand | IDrawPoint;

const EASY_TIME = 30000;
const INTERMEDIATE_TIME = 40000;
const HARD_TIME = 50000;

const MIN_INTERVAL = 40;

interface IStartVirtualPlayer {
  image: string;
  time: number;
  drawMode: DrawingMode;
}

const NAMES: string[] = [
  "Bassam le littéraire",
  "Amine le nerd",
  "Syphax le fumeur",
  "Georges le superviseur",
  "Sami l'uranium",
  "DJ Khaled",
  "Mike Tyson",
  "Donald Trump",
];

export class VirtualPlayer {
  private commands: Command[];
  private current: number;
  private sockets: Socket[];
  private size: number;
  private _stop: boolean;
  public name: string;

  public constructor() {
    this.name = Utils.any(NAMES) + " (joueur virtuel)";
  }

  public startGame(game: IGame, sockets: Socket[]) {
    const time =
      game.difficulty === GameDifficulty.EASY
        ? EASY_TIME
        : game.difficulty === GameDifficulty.INTERMEDIATE
        ? INTERMEDIATE_TIME
        : HARD_TIME;

    this.start(game.image, time, game.drawingMode, sockets);
  }

  public handle(s: Socket) {
    s.on(Events.START_VIRTUAL, (data: IStartVirtualPlayer) =>
      this.start(data.image, data.time, data.drawMode, [s])
    );
  }

  private start(
    image: string,
    time: number,
    drawMode: DrawingMode,
    sockets: Socket[]
  ) {
    const path = `images/${image}`;
    this.sockets = sockets;
    this.current = 0;
    this._stop = false;
    this.load(path, drawMode);
    this.emit(time);
  }

  public stop() {
    this._stop = true;
  }

  private load(path: string, drawMode: DrawingMode) {
    this.commands = path.endsWith("svg")
      ? SvgParser.load(path, drawMode)
      : ImageParser.load(path, drawMode);
  }

  private emit(time: number) {
    const interval = time / this.commands.length;
    this.size =
      interval > MIN_INTERVAL ? 1 : Math.round(MIN_INTERVAL / interval);

    this.next(interval > MIN_INTERVAL ? Math.round(interval) : MIN_INTERVAL);
  }

  private next(interval: number) {
    if (this.current >= this.commands.length || this._stop) return;

    this.broadcast(this.getBatch());

    setTimeout(() => this.next(interval), interval);
  }

  private getBatch(): Command[] {
    const batch = [];
    for (let i = 0; i < this.size && this.current < this.commands.length; i++)
      batch.push(this.commands[this.current++]);

    return batch;
  }

  private broadcast(commands: Command[]) {
    this.sockets.forEach(s => s.emit(Events.DRAW, commands));
  }
}
