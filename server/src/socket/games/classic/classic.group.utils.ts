import { IClassicGroup, IGroupPlayer } from "../../core/types";
import { BaseGroupUtils } from "../base.group.utls";

export class ClassicGroupUtils extends BaseGroupUtils<IClassicGroup> {
  public constructor(group: IClassicGroup) {
    super(group);
  }

  public get players() {
    const players: IGroupPlayer[] = [];

    if (this.g.A.A && this.g.A.A.type === "REAL") players.push(this.g.A.A);
    if (this.g.A.B && this.g.A.B.type === "REAL") players.push(this.g.A.B);
    if (this.g.B.A && this.g.B.A.type === "REAL") players.push(this.g.B.A);
    if (this.g.B.B && this.g.B.B.type === "REAL") players.push(this.g.B.B);

    return players;
  }

  public removePlayer(id: string) {
    let removed = false;

    if (this.checkUser(this.g.A.A, id))
      [this.g.A.A, removed] = [undefined, true];
    if (this.checkUser(this.g.A.B, id))
      [this.g.A.B, removed] = [undefined, true];
    if (this.checkUser(this.g.B.A, id))
      [this.g.B.A, removed] = [undefined, true];
    if (this.checkUser(this.g.B.B, id))
      [this.g.B.B, removed] = [undefined, true];

    return removed;
  }
}
