import { IGroupPlayer, ISocketUser, Socket } from "../core/types";
import { set } from "lodash";

export abstract class BaseGroupUtils<T extends object> {
  protected g: T;

  public constructor(group: T) {
    this.g = group;
  }

  public abstract get players(): IGroupPlayer[];

  public get users() {
    return this.players.map(p => p.user as ISocketUser);
  }

  public get usernames() {
    return this.players.map(p => (p.user as ISocketUser).username);
  }

  public get sockets() {
    return this.players.map(p => (p.user as ISocketUser).s);
  }

  public username(s: Socket) {
    const user = this.users.find(u => u.s.id === s.id);
    return user ? user.username : undefined;
  }

  public set(player: IGroupPlayer, pos?: string, _s?: Socket) {
    set(this.g, pos, player);
  }

  public ready(pos?: string, _s?: Socket) {
    set(this.g, `${pos}.ready`, true);
  }

  public abstract removePlayer(id: string): boolean;

  protected checkUser(player: IGroupPlayer, id: string) {
    return (
      player &&
      player.type === "REAL" &&
      (player.user as ISocketUser).s.id === id
    );
  }
}
