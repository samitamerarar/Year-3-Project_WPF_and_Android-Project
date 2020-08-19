import { ITournamentGroup, IGroupPlayer, Socket } from "../../core/types";
import { BaseGroupUtils } from "../base.group.utls";

export class TournamentGroupUtils extends BaseGroupUtils<ITournamentGroup> {
  public constructor(group: ITournamentGroup) {
    super(group);
  }

  public get players() {
    return this.g.players;
  }

  public removePlayer(id: string) {
    let removed = false;

    const i = this.g.players.findIndex(p => this.checkUser(p, id));

    if (i !== 1) {
      this.g.players.splice(i, 1);
      this.g.ready.splice(i, 1);
      removed = true;
    }

    return removed;
  }

  public set(player: IGroupPlayer, _pos?: string, s?: Socket) {
    this.g.players.push(player);
    this.g.ready.push([s.id, false]);
  }

  public ready(_pos?: string, s?: Socket) {
    const i = this.g.ready.findIndex(r => r[0] === s.id);
    if (i === -1) return;

    this.g.ready[i][1] = true;
    this.g.players[i].ready = true;
  }
}
