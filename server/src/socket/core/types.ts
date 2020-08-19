import { CustomSocket } from "./socket";
import { GameDifficulty } from "../../models/game.model";
import { ISvgCommand } from "../../utils/svg.parser.utils";

export type Socket = CustomSocket;

// CHAT
export interface ISocketUser {
  username: string;
  avatar?: string;
  s: Socket;
}

export interface ISocketChannel {
  name: string;
  users: ISocketUser[];
}

export interface IChannelInteraction {
  username: string;
  channel: string;
}

// GROUPS MANAGEMENT
export interface IVirtualUser {}

export type PlayerType = "REAL" | "VIRTUAL";
export type PlayerRole = "WRITE" | "GUESS" | "PASSIVE";
export type IGroup =
  | IClassicGroup
  | ISoloGroup
  | ICoopGroup
  | ITournamentGroup
  | ITournamentRound;

export enum GameMode {
  CLASSIC = "CLASSIC",
  SOLO = "SOLO",
  COOP = "COOP",
  TOURNAMENT = "TOURNAMENT",
  TOURNAMENT_ROUND = "TOURNAMENT_ROUND",
}

export interface IGroupSubscribers {
  [key: string]: Socket[];
}

export interface IGroupPlayer {
  type: PlayerType;
  user: ISocketUser | IVirtualUser;
  role: PlayerRole;
  ready: boolean;
}

export interface IBaseGroup {
  id: string;
  name: string;
  difficulty: GameDifficulty;
  mode: GameMode;
}

export interface IClassicGroupTeam {
  A?: IGroupPlayer;
  B?: IGroupPlayer;
  points: number;
  role: "ACTIVE" | "PASSIVE";
}

export interface IClassicGroup extends IBaseGroup {
  A: IClassicGroupTeam;
  B: IClassicGroupTeam;
  mode: GameMode.CLASSIC;
}

export interface ICoopGroupTeam {
  points: number;
  A?: IGroupPlayer;
  B?: IGroupPlayer;
  C?: IGroupPlayer;
  D?: IGroupPlayer;
}

export interface ISoloGroup extends IBaseGroup {
  mode: GameMode.SOLO;
  A: ICoopGroupTeam;
}

export interface ICoopGroup extends IBaseGroup {
  mode: GameMode.COOP;
  A: ICoopGroupTeam;
}

export interface ITournamentGroup extends IBaseGroup {
  mode: GameMode.TOURNAMENT;
  players: IGroupPlayer[];
  ready: [string, boolean][];
}

export interface ITournamentRound extends IBaseGroup {
  mode: GameMode.TOURNAMENT_ROUND;
  A: IGroupPlayer;
  B: IGroupPlayer;
}

// GAME MANAGEMENT

export interface IGameState {
  id: string;
  role: PlayerRole | "PASSIVE";
  answer?: string;
  points: number;
  otherPoints?: number;
  clear: boolean;
  currentRound: number;
  counter: boolean;
  startTimer?: number;
  attemptsLeft: number;
}

// USED TO COMMUNICATE WITH SERVER

export interface IGroupForm {
  mode: GameMode;
  difficulty: GameDifficulty;
  name?: string;
}

export interface IAddPlayerForm {
  gid: string;
  pos?: string;
  type: PlayerType;
  username: string;
  avatar: string;
}

export interface IPlayerReadyForm {
  gid: string;
  pos: string;
}

export interface IPointParams {
  pointNature: "ellipse" | "rectangle";
  color?: string;
  width?: number;
  height?: number;
}

export interface IDrawPoint {
  type: "DRAW";
  action: "ADD" | "DEL";
  id: number;
  pointParams: IPointParams;
  point: { x: number; y: number };
}

export type IDraw = IDrawPoint | ISvgCommand;

export interface IWinner {
  winner: boolean;
}
