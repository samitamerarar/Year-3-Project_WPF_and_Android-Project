import { Events } from "../core/events";
import { IChatMessage } from "../../models/channel.model";
import { ChatService } from "../../services/chat.server.service";
import { ISocketChannel, IChannelInteraction, Socket } from "../core/types";
import { AuthService } from "../../services/auth.server.service";
import { GameChatSocketHandler } from "./game.chat.socket.handler";

export class ChatSocketHandler {
  private channels: ISocketChannel[];
  private chatService: ChatService;
  private authService: AuthService;
  private gameChat: GameChatSocketHandler;

  public constructor(gameChat: GameChatSocketHandler) {
    this.channels = [];
    this.chatService = new ChatService();
    this.authService = new AuthService();
    this.gameChat = gameChat;
  }

  public handle(s: Socket) {
    s.on(Events.INIT_CHAT, (username: string) => {
      this.initChat(username, s);
    });

    s.on(Events.ENTER_CHANNEL, (it: IChannelInteraction) => {
      this.enterChannel(it.channel, it.username, s);
    });

    s.on(Events.LEAVE_CHANNEL, (it: IChannelInteraction) => {
      this.leaveChannel(it.channel, it.username);
    });

    s.on(Events.NEW_MESSAGE, (message: IChatMessage) => {
      if (message.channel === "game") this.gameChat.message(s, message);
      else this.newMessage(message);
    });
  }

  public disconnect(s: Socket) {
    this.channels.forEach((c, i, a) => {
      const idx = c.users.findIndex(user => user.s.id === s.id);
      if (idx !== -1) a[i].users.splice(idx, 1);
    });
  }

  private async initChat(username: string, s: Socket) {
    const userStatus = await this.authService.getUser(username);
    if (!userStatus[0]) return;

    const channels = userStatus[1].channels!;
    const promises = channels.map(channel =>
      this.enterChannel(channel, username, s)
    );
    await Promise.all(promises);
  }

  private async enterChannel(name: string, username: string, s: Socket) {
    console.log("Enter Channel");

    const channelStatus = await this.chatService.getChannel(name);
    if (!channelStatus[0]) return;

    const channel = this.channels.find(c => c.name === name);

    if (!channel) this.channels.push({ name, users: [{ username, s }] });
    else if (channel.users.findIndex(u => u.username === username) === -1)
      channel.users.push({ username, s });

    console.log(`${username} entered channel ${name}`);
  }

  private async leaveChannel(name: string, username: string) {
    const channel = this.channels.find(c => c.name === name);
    if (!channel) return;

    const idx = channel.users.findIndex(user => user.username === username);
    if (idx === -1) return;

    channel.users.splice(idx, 1);
    console.log(`${username} left channel ${name}`);
  }

  private async newMessage(message: IChatMessage) {
    const channel = this.channels.find(c => c.name === message.channel);
    if (!channel) return;

    if (!(await this.chatService.addMessage(message))) return;

    channel.users.forEach(user => user.s.emit(Events.NEW_MESSAGE, message));

    console.log(
      `${message.author} sent a new message in channel ${message.channel}`
    );
  }
}
