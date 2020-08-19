import {
  Channel,
  IChannel,
  IChannelDocument,
  IChatMessage,
} from "../models/channel.model";
import { AuthService } from "./auth.server.service";
import { ServiceStatus } from "./core/interfaces";
import { isArray } from "util";
import { IUserDocument } from "models/user.model";

type ChatStatus = ServiceStatus<string[]>;

export class ChatService {
  private authService: AuthService;

  public constructor() {
    this.authService = new AuthService();
  }

  public async createChannel(name: string): Promise<ChatStatus> {
    if (name === "game")
      return { code: 409, msg: "Channel Already Exists!", data: null };

    const channel = new Channel({ name });
    try {
      await channel.save();
      return { code: 200, msg: "Created!", data: null };
    } catch (e) {
      return { code: 409, msg: "Channel Already Exists!", data: null };
    }
  }

  public async deleteChannel(name: string) {
    if (["game", "general"].includes(name))
      return { code: 409, msg: "Channel Cannot be deleted!", data: null };

    try {
      await Channel.deleteOne({ name }).exec();
      return { code: 200, msg: "Deleted!", data: null };
    } catch (e) {
      return { code: 404, msg: "Channel Doesn't Exists!", data: null };
    }
  }

  public async joinChannel(
    username: string,
    channelName: string | string[]
  ): Promise<ChatStatus> {
    const channelNames: string[] = isArray(channelName)
      ? channelName
      : [channelName];

    console.log(channelNames);
    const status = await this.checkChannels(channelNames, username);
    console.log(status);

    if (status[0]) {
      return { code: 404, msg: `${status[0]} Not Found!`, data: null };
    }

    const user = status[1];
    channelNames.forEach(name => {
      if (user.channels.indexOf(name) === -1) {
        user.channels.push(name);
      }
    });

    await user.save();

    return { code: 200, msg: "Channel Joined!", data: null };
  }

  public async getChannels() {
    return await Channel.find().exec();
  }

  public async addMessage(message: IChatMessage) {
    const channelStatus = await this.getChannel(message.channel);
    if (!channelStatus[0]) return false;

    message.created = new Date().toLocaleString();

    const chat = channelStatus[1].chat;
    chat.push(message);
    channelStatus[1].save();

    return true;
  }

  public async getChannel(name: string): Promise<[boolean, IChannelDocument?]> {
    let channel: IChannelDocument;

    try {
      channel = await Channel.findOne({ name }).exec();
      if (channel === null) throw new Error("");
    } catch (e) {
      return [false, undefined];
    }

    return [true, channel];
  }

  public async removeUser(username: string, ch: string) {
    const [status, channel] = await this.getChannel(ch);
    if (!status) return;

    channel.chat = channel.chat.filter(c => c.author !== username);
    await channel.save();
  }

  private async checkChannels(
    channels: string[],
    username: string
  ): Promise<[string | null, IUserDocument?]> {
    const channelStatus = await Promise.all(
      channels.map(c => this.getChannel(c))
    );
    const userStatus = await this.authService.getUser(username);

    console.log(channelStatus);

    const errorStatus = channelStatus.some(c => !c[0])
      ? "Channel"
      : !userStatus[0]
      ? "User"
      : null;

    return [errorStatus, userStatus[1]];
  }
}
