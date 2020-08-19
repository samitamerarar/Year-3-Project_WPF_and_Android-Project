import { ITournamentRound, IGroupPlayer, Socket } from "../../core/types";
import { BaseGroupUtils } from "../base.group.utls";

export class TournamentRoundUtils extends BaseGroupUtils<ITournamentRound> {
  public constructor(group: ITournamentRound) {
    super(group);
  }

  public get players() {
    return [this.g.A, this.g.B];
  }

  public removePlayer(id: string) {
    let removed = false;
    if (this.checkUser(this.g.A, id)) [this.g.A, removed] = [undefined, true];
    if (this.checkUser(this.g.B, id)) [this.g.B, removed] = [undefined, true];

    return removed;
  }
}
