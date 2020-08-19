import { IUser, User, IUserDocument } from "../models/user.model";
import * as bcrypt from "bcrypt";
import { ServiceStatus } from "./core/interfaces";
import { pick } from "./core/picker";
import { IMatch, GameMode } from "../models/match.model";
import { GameService } from "./game.server.service";
import { meanBy, sumBy, maxBy } from "lodash";
import { ChatService } from "./chat.server.service";

const SALT_ROUNDS = 10;
type AuthStatus = ServiceStatus<IUser>;
type ProfileStatus = ServiceStatus<IUserProfile>;

interface IProfileUpdate {
  firstName?: string;
  lastName?: string;
  avatar?: string;
}

interface IGameHistory extends IMatch {
  localStart: string;
  localEnd: string;
}

interface IUserProfile extends IProfileUpdate {
  username: string;
  matchsPlayed: number;
  victoryPercent: number;
  averageMatchDuration: number;
  totalMatchDuration: number;
  soloHighestScore: number;
  coopHighestScore: number;
  tournamentsWon: number;
  history: { login: string; logout: string }[];
  gameHistory: IGameHistory[];
}

export class AuthService {
  public validate(body: any) {
    return body.username !== undefined && body.password !== undefined;
  }

  public async login(username: string, password: string): Promise<AuthStatus> {
    if (!username || !password) {
      return { code: 400, msg: "Invalid parameters", data: null };
    }

    const status = await this.getUser(username);

    if (!status[0]) {
      return { code: 404, msg: "Username Not Found", data: null };
    }

    const user: IUserDocument = status[1];

    console.log(user);

    if (!(await bcrypt.compare(password, user.password))) {
      return {
        code: 401,
        msg: "Username and password don't match",
        data: null,
      };
    }

    if (user.connected) {
      return {
        code: 409,
        msg: "User already connected.",
        data: null,
      };
    }

    console.log("User Logged In");
    user.loginTimes.push(new Date(Date.now()).toLocaleString());
    user.connected = true;
    await user.save();
    return { code: 200, msg: "Logged In!", data: pick(IUser, user) };
  }

  public async register(username: string, password: string) {
    password = await bcrypt.hash(password, SALT_ROUNDS);
    const user = new User({ username, password });
    await user.save();

    console.log("User Created");
    return pick(IUser, user);
  }

  public async getUser(username: string): Promise<[boolean, IUserDocument?]> {
    let user: IUserDocument;

    try {
      user = await User.findOne({ username }).exec();
      if (user === null) throw new Error("");
    } catch (e) {
      return [false, undefined];
    }

    return [true, user];
  }

  public async updateProfile(username: string, u: IProfileUpdate) {
    const [status, user] = await this.getUser(username);

    if (!status) {
      return { code: 404, msg: "Username Not Found", data: null };
    }

    try {
      user.firstName = u.firstName ? u.firstName : user.firstName;
      user.lastName = u.lastName ? u.lastName : user.lastName;
      user.avatar = u.avatar ? u.avatar : user.avatar;
      await user.save();
      return { code: 200, msg: "Profile Updated!", data: null };
    } catch (e) {
      return { code: 400, msg: "Malformed request", data: null };
    }
  }

  public async getProfile(username: string): Promise<ProfileStatus> {
    const [status, user] = await this.getUser(username);
    if (!status) return { code: 404, msg: "Username Not Found", data: null };

    const { firstName, lastName, avatar, loginTimes, logoutTimes } = user;
    const matches = await new GameService().getMatches(username);

    const classic = matches.filter(m => m.mode === GameMode.CLASSIC);
    const solo = matches.filter(m => m.mode === GameMode.SOLO);
    const coop = matches.filter(m => m.mode === GameMode.COOP);
    const tRound = matches.filter(m => m.mode === GameMode.TOURNAMENT_ROUND);
    const t = matches.filter(m => m.mode === GameMode.TOURNAMENT);

    const matchsPlays = [...classic, ...solo, ...coop, ...tRound];

    const v = [
      ...classic.filter(m => m.resultClassic.w.includes(username)),
      ...tRound.filter(m => m.resultClassic.w.includes(username)),
    ];
    const vmatchlength = classic.length + tRound.length;

    const highestSolo = maxBy(solo, m => m.resultCoop);
    const highestCoop = maxBy(coop, m => m.resultCoop);

    const tournamentsWon = t.filter(m => m.resultClassic.w.includes(username))
      .length;

    const history = loginTimes.map((l, i) => {
      return {
        login: l || "",
        logout: logoutTimes[i] || "",
      };
    });

    const gameHistory = matches.map(m => {
      return {
        ...pick(IMatch, m),
        localStart: new Date(m.start).toLocaleString(),
        localEnd: new Date(m.end).toLocaleString(),
      };
    });

    const data: IUserProfile = {
      username,
      firstName,
      lastName,
      avatar,
      matchsPlayed: matchsPlays.length,
      victoryPercent: vmatchlength === 0 ? 0 : v.length / vmatchlength,
      averageMatchDuration: meanBy(matchsPlays, m => m.duration) || 0,
      totalMatchDuration: sumBy(matchsPlays, m => m.duration),
      soloHighestScore: highestSolo ? highestSolo.resultCoop : 0,
      coopHighestScore: highestCoop ? highestCoop.resultCoop : 0,
      tournamentsWon,
      history,
      gameHistory,
    };

    return { code: 200, msg: "", data };
  }

  public async logout(username: string) {
    const [status, user] = await this.getUser(username);
    if (!status) return;

    user.logoutTimes.push(new Date(Date.now()).toLocaleString());
    user.connected = false;
    user.save();
  }

  public async tutorial(username: string, tutorial: string) {
    const [status, user] = await this.getUser(username);
    if (!status) return { code: 404, msg: "Username Not Found", data: null };

    const tutorials = { ...user.tutorials! };
    tutorials[tutorial] = false;
    user.tutorials = tutorials;
    await user.save();

    return { code: 200, msg: "Tutorial Done!", data: null };
  }

  public async deleteUser(username: string) {
    const [status, user] = await this.getUser(username);
    if (!status) return { code: 404, msg: "Username Not Found", data: null };

    const chat = new ChatService();
    const promises = user.channels.map(c => chat.removeUser(username, c));
    await Promise.all(promises);

    await User.deleteOne({ username }).exec();

    return { code: 200, msg: "User deleted", data: null };
  }
}
