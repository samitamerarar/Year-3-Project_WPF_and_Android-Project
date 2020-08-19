import { Utils } from "./utils";
import * as fs from "fs";
import { DrawingMode } from "../models/game.model";
import { Sort } from "./sort.utils";
import { flatten } from "lodash";

export class ISvgCommand {
  type: "PATH";
  command?: string;
  args: number[];
}

const CANVAS_WIDTH = 800;
const CANVAS_HEIGHT = 750;

export class SvgParser {
  public static load(filepath: string, drawMode?: DrawingMode) {
    const svg = fs.readFileSync(filepath, "utf8");
    let path = svg.match(/<path\b([\s\S]*?)\/>/g)[0];
    path = path.match(/="(.*?)"/g)[0];
    path = path
      .replace(/"/g, "")
      .replace(/=/g, "")
      .trim();

    const w = parseFloat(
      svg.match(/^<svg[^>]*width\s*=\s*\"?(\d+)\"?[^>]*>/)[1]
    );
    const h = parseFloat(
      svg.match(/^<svg[^>]*height\s*=\s*\"?(\d+)\"?[^>]*>/)[1]
    );

    return flatten(Sort.sort(this.parse(path, w, h), drawMode));
  }

  private static parse(path: string, w: number, h: number) {
    const sx = CANVAS_WIDTH / w;
    const sy = CANVAS_HEIGHT / h;
    return this.convert(Parser.parse(path), sx, sy);
  }

  private static convert(
    commands: string[][],
    sx = 1,
    sy = 1
  ): ISvgCommand[][] {
    const svgCommands = [];

    commands.forEach(command => {
      svgCommands.push([
        {
          type: "PATH",
          command: command[0],
          args: [parseFloat(command[1]) * sx, parseFloat(command[2]) * sy],
        },
        {
          type: "PATH",
          command: command[3],
          args: command
            .slice(4)
            .map((s, i) =>
              i % 2 === 0 ? sx * parseFloat(s) : sy * parseFloat(s)
            ),
        },
      ]);
    });

    return svgCommands;
  }
}

class Parser {
  public static parse(a: string) {
    const v = a.replace(/,/g, "");
    const b = a.split(" ");
    const c: string[][] = [];
    let d: string[] = [];

    let lastCommand: string = "";
    b.forEach(elem => {
      if (elem === "M" || elem === "L" || elem === "C") {
        if (d.length > 0) {
          c.push(d);
          d = [];
        }
        d.push(elem);
        lastCommand = elem;
      } else {
        if (lastCommand === "L" && d.length > 2) {
          c.push(d);
          d = [];
          d.push(lastCommand);
        }
        d.push(elem);
      }
    });
    c.push(d);

    const z: string[][] = [];
    let p: string[] = [];
    c.forEach(elem => {
      switch (elem[0]) {
        case "M": {
          if (p.length > 0) {
            p = [];
          }
          p.push(elem[0], elem[1], elem[2]);
          break;
        }
        case "L": {
          if (p.length > 0) {
            p.push(elem[0], elem[1], elem[2]);
            z.push(p);
            p = [];
          }
          p.push("M", elem[1], elem[2]);
          break;
        }
        case "C": {
          if (p.length > 0) {
            p.push(
              elem[0],
              elem[1],
              elem[2],
              elem[3],
              elem[4],
              elem[5],
              elem[6]
            );
            z.push(p);
            p = [];
          }
          p.push("M", elem[5], elem[6]);
          break;
        }

        default:
          break;
      }
    });

    return z;
  }
}
