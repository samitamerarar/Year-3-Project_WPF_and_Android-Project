import {
  IGroup,
  Socket,
  ISocketUser,
  IGroupForm,
  IAddPlayerForm,
  IBaseGroup,
  GameMode,
  IClassicGroup,
  IPlayerReadyForm,
  IGroupSubscribers,
  IGroupPlayer,
  PlayerRole,
} from "../core/types";
import * as uid from "uniqid";
import { Events } from "../core/events";
import { get } from "lodash";
import { GameSocketHandler } from "./game.socket.handler";
import { Utils } from "../../utils/utils";
import { GameDifficulty } from "../../models/game.model";
import { GroupUtils } from "../games/group.utils";
import { GameChatSocketHandler } from "./game.chat.socket.handler";

export class GroupsSocketHandler {
  private groups: IGroup[];
  private gameHandler: GameSocketHandler;
  private subscribers: IGroupSubscribers;
  private gameChat: GameChatSocketHandler;

  public constructor(
    gameHandler: GameSocketHandler,
    gameChat: GameChatSocketHandler
  ) {
    this.groups = [];
    this.subscribers = {};
    this.gameHandler = gameHandler;
    this.gameChat = gameChat;
  }

  public handle(s: Socket) {
    s.on(Events.LIST_GROUPS, (f: IGroupForm) => this.list(f, s));

    s.on(Events.NEW_GROUP, (f: IGroupForm) => this.create(f));

    s.on(Events.ADD_PLAYER, (f: IAddPlayerForm) => this.addPlayer(f, s));

    s.on(Events.PLAYER_READY, (f: IPlayerReadyForm) => this.signalReady(f, s));

    s.on(Events.UNSUBSCRIBE, (f: IGroupForm) => this.unsubscribe(f, s));

    s.on(Events.LEAVE_GROUP, () => this.disconnect(s));
  }

  public disconnect(s: Socket) {
    this.groups.forEach(group => {
      GroupUtils.use(group).removePlayer(s.id);
    });

    Object.keys(this.subscribers).forEach(key => {
      const pair = key.split(".");
      const f = {
        mode: GameMode[pair[0]],
        difficulty: GameDifficulty[pair[1]],
      };
      this.unsubscribe(f, s);
      this.notifySubscribers(f);
    });

    this.gameChat.remove(s);
  }

  private list(f: IGroupForm, s: Socket) {
    const groups = this.groups.filter(
      g => g.mode === f.mode && g.difficulty === f.difficulty
    );

    const clean = JSON.parse(Utils.filterStringify(groups, "s"));
    console.log(clean);

    s.emit(Events.LIST_GROUPS, clean);

    if (!this.subscribers[`${f.mode}.${f.difficulty}`]) {
      this.subscribers[`${f.mode}.${f.difficulty}`] = [];
    }

    const i = this.subscribers[`${f.mode}.${f.difficulty}`].findIndex(
      sock => sock.id === s.id
    );

    if (i === -1) {
      this.subscribers[`${f.mode}.${f.difficulty}`].push(s);
    }
  }

  private create(f: IGroupForm) {
    const group: IBaseGroup = {
      id: uid("game_id_"),
      name: f.name,
      difficulty: f.difficulty,
      mode: f.mode,
    };

    this.groups.push(this.completeGroup(group));
    this.notifySubscribers(f);
  }

  private completeGroup(group: IBaseGroup): IGroup {
    switch (group.mode) {
      case GameMode.CLASSIC:
        return {
          ...group,
          mode: GameMode.CLASSIC,
          A: { points: 0, role: "ACTIVE" },
          B: { points: 0, role: "PASSIVE" },
        };

      case GameMode.SOLO:
        return {
          ...group,
          mode: GameMode.SOLO,
          A: { points: 0 },
        };

      case GameMode.COOP:
        return {
          ...group,
          mode: GameMode.COOP,
          A: { points: 0 },
        };

      case GameMode.TOURNAMENT:
        return {
          ...group,
          mode: GameMode.TOURNAMENT,
          players: [],
          ready: [],
        };

      default:
        console.log("INVALID GROUP TYPE");
        return;
    }
  }

  private addPlayer(f: IAddPlayerForm, s: Socket) {
    if (
      f.type === "REAL" &&
      this.groups.some(g => GroupUtils.use(g).usernames.includes(f.username))
    ) {
      console.log("ADD PLAYER TO GROUP: PLAYER IN ANOTHER GROUP", f.gid, f.pos);
      return;
    }

    const group = this.groups.find(g => g.id === f.gid);

    console.log(f);

    if (!group) {
      console.log("ADD PLAYER TO GROUP: INVALID PARAMETERS", f.gid, f.pos);
      return;
    }

    if (group.mode !== GameMode.CLASSIC && f.type === "VIRTUAL") {
      console.log("No virtual players are allowed in solo and coop games.");
      return;
    }

    if (group.mode !== GameMode.TOURNAMENT && get(group, f.pos)) {
      console.log("Player already in position", f.gid, f.pos);
      return;
    }

    if (group.mode === GameMode.TOURNAMENT && group.players.length >= 16) {
      console.log("Tournament reached maximum participants", f.gid, f.pos);
      return;
    }

    let role: PlayerRole;
    if (group.mode === GameMode.CLASSIC)
      role = f.pos.split(".")[1] === "A" ? "GUESS" : "WRITE";
    else role = "GUESS";

    if (f.type !== "REAL" && role === "GUESS") {
      console.log("Virtual player cannot guess.", f.gid, f.pos);
      return;
    }

    const user =
      f.type === "REAL" ? { s, username: f.username, avatar: f.avatar } : {};
    const ready = f.type !== "REAL";
    const player: IGroupPlayer = { role, type: f.type, user, ready };
    GroupUtils.use(group).set(player, f.pos, s);

    this.notifySubscribers(group);

    console.log(this.groups);

    if (player.type === "VIRTUAL") this.startGame(group);
    else this.gameChat.putChannel(group);
  }

  private signalReady(f: IPlayerReadyForm, s: Socket) {
    const group = this.groups.find(g => g.id === f.gid);

    if (!group) {
      console.log("PLAYER READY: INVALID PARAMETERS", f.gid, f.pos);
      return;
    }

    if (group.mode !== GameMode.TOURNAMENT && !get(group, f.pos)) {
      console.log("No player is in this position", f.gid, f.pos);
      return;
    }

    GroupUtils.use(group).ready(f.pos, s);

    const { mode, difficulty } = group;
    this.notifySubscribers({ mode, difficulty });
    this.startGame(group);
  }

  private startGame(group: IGroup) {
    if (this.checkGroupReady(group)) {
      this.gameHandler.startGame(group);
      this.groups = this.groups.filter(g => g.id !== group.id);
      this.unsubscribeGroup(group);
    }
  }

  private checkGroupReady(group: IGroup) {
    switch (group.mode) {
      case GameMode.CLASSIC:
        return (
          group.A.A &&
          group.A.A.ready &&
          group.A.B &&
          group.A.B.ready &&
          group.B.A &&
          group.B.A.ready &&
          group.B.B &&
          group.B.B.ready
        );

      case GameMode.SOLO:
        return group.A.A && group.A.A.ready;

      case GameMode.COOP:
        let condition = GroupUtils.use(group).players.length > 1;
        if (group.A.A) condition = condition && group.A.A.ready;
        if (group.A.B) condition = condition && group.A.B.ready;
        if (group.A.C) condition = condition && group.A.C.ready;
        if (group.A.D) condition = condition && group.A.D.ready;
        return condition;

      case GameMode.TOURNAMENT:
        let c = [2, 4, 8, 16].includes(group.players.length);
        c = c && group.ready.filter(r => r[1]).length === group.players.length;
        return c;

      default:
        return false;
    }
  }

  private notifySubscribers(f: IGroupForm) {
    if (this.subscribers[`${f.mode}.${f.difficulty}`]) {
      this.subscribers[`${f.mode}.${f.difficulty}`].forEach(s =>
        this.list(f, s)
      );
    }
  }

  private unsubscribeGroup(group: IGroup) {
    const f: IGroupForm = { mode: group.mode, difficulty: group.difficulty };

    switch (group.mode) {
      case GameMode.CLASSIC:
        const g = group as IClassicGroup;
        if (g.A.A && g.A.A.type === "REAL")
          this.unsubscribe(f, (g.A.A.user as ISocketUser).s);
        if (g.A.B && g.A.B.type === "REAL")
          this.unsubscribe(f, (g.A.B.user as ISocketUser).s);
        if (g.B.A && g.B.A.type === "REAL")
          this.unsubscribe(f, (g.B.A.user as ISocketUser).s);
        if (g.B.B && g.B.B.type === "REAL")
          this.unsubscribe(f, (g.B.B.user as ISocketUser).s);
        return;

      case GameMode.COOP:
      case GameMode.SOLO:
        if (group.A.A) this.unsubscribe(f, (group.A.A.user as ISocketUser).s);
        if (group.A.B) this.unsubscribe(f, (group.A.B.user as ISocketUser).s);
        if (group.A.C) this.unsubscribe(f, (group.A.C.user as ISocketUser).s);
        if (group.A.D) this.unsubscribe(f, (group.A.D.user as ISocketUser).s);
        return;

      case GameMode.TOURNAMENT:
        group.players.forEach(p =>
          this.unsubscribe(f, (p.user as ISocketUser).s)
        );
        return;

      default:
        return;
    }
  }

  private unsubscribe(f: IGroupForm, s: Socket) {
    if (this.subscribers[`${f.mode}.${f.difficulty}`]) {
      this.subscribers[`${f.mode}.${f.difficulty}`] = this.subscribers[
        `${f.mode}.${f.difficulty}`
      ].filter(sock => sock.id !== s.id);
    }
  }
}
