// tslint:disable-next-line:no-require-imports
import potrace = require("potrace");
import * as fs from "fs";

export class PotraceService {
  public static async trace(
    input: string,
    output: string,
    params?: PotraceOptions
  ) {
    if (params) {
      return this.traceWithParams(input, output, params);
    } else {
      return this.traceWithoutParams(input, output);
    }
  }

  private static async traceWithParams(
    input: string,
    output: string,
    params: PotraceOptions
  ) {
    return new Promise<void>((resolve, reject) => {
      potrace.trace(input, params, (err: any, svg: any) => {
        if (err) reject(err);
        fs.writeFileSync(output, svg);
        resolve();
      });
    });
  }

  private static async traceWithoutParams(input: string, output: string) {
    return new Promise<void>((resolve, reject) => {
      potrace.trace(input, (err: any, svg: any) => {
        if (err) reject(err);
        fs.writeFileSync(output, svg);
        resolve();
      });
    });
  }
}

export interface PotraceOptions {
  background: string;
  color: string;
  threshold: number;
}
