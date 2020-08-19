import { ISvgCommand } from "./svg.parser.utils";
import { DrawingMode } from "../models/game.model";
import { shuffle } from "lodash";
import { IDrawPoint } from "socket/core/types";

type Command = ISvgCommand | IDrawPoint;

interface Point {
  i: number;
  x: number;
  y: number;
}

const CENTER = { x: 400, y: 325 };

export class Sort {
  public static sort(commands: Command[][], drawMode?: DrawingMode) {
    return this._sort(this.extractPoints(commands), drawMode).map(
      p => commands[p.i]
    );
  }

  private static extractPoints(commands: Command[][]) {
    return commands.map((c, i) => this.extractPoint(c, i));
  }

  private static extractPoint(commands: Command[], i: number): Point {
    if (commands[0].type === "PATH")
      return { x: commands[0].args[0], y: commands[0].args[1], i };
    else return { x: commands[0].point.x, y: commands[0].point.y, i };
  }

  private static _sort(points: Point[], drawMode?: DrawingMode) {
    if (!drawMode) return points;

    switch (drawMode) {
      case DrawingMode.PANORAMIC_R:
        return points.sort((a, b) => a.x - b.x);

      case DrawingMode.PANORAMIC_L:
        return points.sort((a, b) => b.x - a.x);

      case DrawingMode.PANORAMIC_D:
        return points.sort((a, b) => a.y - b.y);

      case DrawingMode.PANORAMIC_U:
        return points.sort((a, b) => b.y - a.y);

      case DrawingMode.CENTERED_IN:
        return points.sort((a, b) => this.d(b) - this.d(a));

      case DrawingMode.CENTERED_OUT:
        return points.sort((a, b) => this.d(a) - this.d(b));

      case DrawingMode.RANDOM:
        return shuffle(points);

      case DrawingMode.NORMAL:
      default:
        return points;
    }
  }

  private static d(p: Point) {
    return Math.sqrt(this.sq(CENTER.x - p.x) + this.sq(CENTER.y - p.y));
  }

  private static sq(x: number) {
    return x * x;
  }
}
