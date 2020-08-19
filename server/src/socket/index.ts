import { Server } from "http";
import * as socket from "socket.io";
import { ChatSocketHandler } from "./handlers/chat.socket.handler";
import { CustomSocket } from "./core/socket";
import { GroupsSocketHandler } from "./handlers/groups.socket.handler";
import { GameSocketHandler } from "./handlers/game.socket.handler";
import { GameChatSocketHandler } from "./handlers/game.chat.socket.handler";
import { VirtualPlayer } from "./games/virtual.player";
import { AuthService } from "../services/auth.server.service";
import * as fs from "fs";

interface HashMap<K> {
  [key: string]: K;
}

export class Socket {
  public io: socket.Server;
  private chatHandler: ChatSocketHandler;
  private groupHandler: GroupsSocketHandler;
  private gameHandler: GameSocketHandler;
  private gameChat: GameChatSocketHandler;
  private globalVirtualPlayer: VirtualPlayer;
  private userSocketMap: HashMap<string>;
  private users: string[];

  constructor(http: Server) {
    this.io = socket(http);
    this.userSocketMap = {};
    this.gameChat = new GameChatSocketHandler();
    this.chatHandler = new ChatSocketHandler(this.gameChat);
    this.gameHandler = new GameSocketHandler(this.gameChat);
    this.groupHandler = new GroupsSocketHandler(
      this.gameHandler,
      this.gameChat
    );
    this.globalVirtualPlayer = new VirtualPlayer();
    this.users = JSON.parse(fs.readFileSync("users.json", "utf8"));
    this.users.forEach(u => this.logoutUser(u));
    this.connect();
  }

  public connect() {
    this.io.on("connection", (s: socket.Socket) => {
      console.log(`connected : ${s.id}`);
      if (!this.userSocketMap[s.id]) {
        this.setUsername(s);
        this.handlers(s, new CustomSocket(s));
      } else {
        console.log("User already Connected");
      }
    });
  }

  public setUsername(s: socket.Socket) {
    this.userSocketMap[s.id] = s.handshake.query.username;
    if (this.userSocketMap[s.id]) this.addUser(this.userSocketMap[s.id]);

    s.on("username", (username: string) => {
      this.userSocketMap[s.id] = username;
      this.addUser(username);
    });
  }

  public handlers(s: socket.Socket, cs: CustomSocket) {
    this.chatHandler.handle(cs);
    this.groupHandler.handle(cs);
    this.globalVirtualPlayer.handle(cs);
    this.disconnectHandler(s, cs);
  }

  public disconnectHandler(s: socket.Socket, cs: CustomSocket) {
    s.on("disconnect", () => {
      this.chatHandler.disconnect(cs);
      this.groupHandler.disconnect(cs);
      this.gameHandler.disconnect(cs);
      this.logoutUser(this.userSocketMap[s.id]);
      delete this.userSocketMap[s.id];
      console.log(`Socket disconnected : ${s.id}`);
    });
  }

  private async logoutUser(username: string) {
    console.log("LOGGING OUT " + username);
    await new AuthService().logout(username);
    this.removeUser(username);
  }

  private addUser(username: string) {
    this.users.push(username);
    fs.writeFileSync("users.json", JSON.stringify(this.users));
  }

  private removeUser(username: string) {
    this.users = this.users.filter(u => u !== username);
    fs.writeFileSync("users.json", JSON.stringify(this.users));
  }
}
