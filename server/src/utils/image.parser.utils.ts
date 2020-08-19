import * as fs from "fs";
import {
  IImageStructre,
  IImageSegment,
  IImagePoint,
} from "../services/core/interfaces";
import { IDrawPoint } from "../socket/core/types";
import { DrawingMode } from "../models/game.model";
import { Sort } from "./sort.utils";
import { flatten } from "lodash";

export class ImageParser {
  public static load(path: string, drawMode: DrawingMode) {
    const image = JSON.parse(fs.readFileSync(path, "utf8")) as IImageStructre;

    const points = image.segments.map((s, i) =>
      s.points.map(p => this.map(s, i, p))
    );

    return flatten(Sort.sort(points, drawMode));
  }

  private static map(s: IImageSegment, i: number, p: IImagePoint): IDrawPoint {
    return {
      type: "DRAW",
      action: "ADD",
      id: i,
      pointParams: {
        pointNature: s.pointNature,
        color: s.color,
        width: s.width,
        height: s.height,
      },
      point: p,
    };
  }
}
