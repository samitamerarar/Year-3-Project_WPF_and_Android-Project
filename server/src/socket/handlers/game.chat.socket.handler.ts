import { IGroup, Socket } from "../core/types";
import { IChatMessage } from "../../models/channel.model";
import { Events } from "../core/events";
import { GroupUtils } from "../games/group.utils";
import { ChatService } from "../../services/chat.server.service";

interface GameChannels {
  [key: string]: Socket[];
}

export class GameChatSocketHandler {
  private channels: GameChannels;
  private chatService: ChatService;

  public constructor() {
    this.channels = {};
    this.chatService = new ChatService();
  }

  public async putChannel(group: IGroup) {
    const id = group.id;
    if (!this.channels[id]) await this.chatService.createChannel(id);
    this.channels[id] = GroupUtils.use(group).sockets.map(s => s);
  }

  public async deleteChannel(id: string) {
    await this.chatService.deleteChannel(id);
    delete this.channels[id];
  }

  public message(s: Socket, message: IChatMessage) {
    const id = this.find(s);
    if (id) this.newMessage(id, message);
  }

  public async newMessage(id: string, message: IChatMessage) {
    if (!this.channels[id]) return;

    const { author, content } = message;
    const channel = id;

    if (!(await this.chatService.addMessage({ author, content, channel })))
      return;

    this.channels[id].forEach(s => s.emit(Events.NEW_MESSAGE, message));
  }

  public remove(s: Socket) {
    Object.keys(this.channels).forEach(
      id =>
        (this.channels[id] = this.channels[id].filter(sock => sock.id !== s.id))
    );
  }

  private find(s: Socket) {
    return Object.keys(this.channels)
      .reverse() // It'll take the latest created channel maybe create a stack to make sure of the order
      .find(id => this.channels[id].some(sock => sock.id === s.id));
  }
}
