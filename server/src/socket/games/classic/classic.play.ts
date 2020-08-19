import { IClassicGroup } from "../../core/types";
import { get } from "lodash";

type ClassicGameRole = "WRITE" | "GUESS" | "PASSIVE";

export class ClassicPlay {
  public A: "ACTIVE" | "PASSIVE";
  public B: "ACTIVE" | "PASSIVE";
  private AA: ClassicGameRole;
  private AB: ClassicGameRole;
  private BA: ClassicGameRole;
  private BB: ClassicGameRole;

  constructor(active: "A" | "B", writer: "A" | "B") {
    this.A = active === "A" ? "ACTIVE" : "PASSIVE";
    this.B = active === "B" ? "ACTIVE" : "PASSIVE";
    this.AA = active === "A" ? (writer === "A" ? "WRITE" : "GUESS") : "PASSIVE";
    this.AB = active === "A" ? (writer === "B" ? "WRITE" : "GUESS") : "PASSIVE";
    this.BA = active === "B" ? (writer === "A" ? "WRITE" : "GUESS") : "PASSIVE";
    this.BB = active === "B" ? (writer === "B" ? "WRITE" : "GUESS") : "PASSIVE";
  }

  public role(pos: string, group: IClassicGroup) {
    let outer = pos[0] + ".B.type";
    const mandatory = pos.endsWith("A") ? "GUESS" : "WRITE";

    return get(this, pos) === "PASSIVE"
      ? get(this, pos)
      : get(group, outer) === "REAL"
      ? get(this, pos)
      : mandatory;
  }
}
