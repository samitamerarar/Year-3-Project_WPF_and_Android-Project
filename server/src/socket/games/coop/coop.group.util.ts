import { ICoopGroup, IGroupPlayer, ISoloGroup } from "../../core/types";
import { BaseGroupUtils } from "../base.group.utls";

export class CoopGroupUtils extends BaseGroupUtils<ICoopGroup | ISoloGroup> {
  public constructor(group: ICoopGroup | ISoloGroup) {
    super(group);
  }

  public get players() {
    const players: IGroupPlayer[] = [];

    if (this.g.A.A && this.g.A.A.type === "REAL") players.push(this.g.A.A);
    if (this.g.A.B && this.g.A.B.type === "REAL") players.push(this.g.A.B);
    if (this.g.A.C && this.g.A.C.type === "REAL") players.push(this.g.A.C);
    if (this.g.A.D && this.g.A.D.type === "REAL") players.push(this.g.A.D);

    return players;
  }

  public removePlayer(id: string) {
    let removed = false;

    if (this.checkUser(this.g.A.A, id))
      [this.g.A.A, removed] = [undefined, true];
    if (this.checkUser(this.g.A.B, id))
      [this.g.A.B, removed] = [undefined, true];
    if (this.checkUser(this.g.A.C, id))
      [this.g.A.C, removed] = [undefined, true];
    if (this.checkUser(this.g.A.D, id))
      [this.g.A.D, removed] = [undefined, true];

    return removed;
  }
}
