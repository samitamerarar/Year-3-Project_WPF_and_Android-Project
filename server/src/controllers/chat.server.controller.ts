import { Request, Response } from "express";
import { ChatService } from "../services/chat.server.service";

export default class ChatController {
  public async createChannel(req: Request, res: Response, _next: Function) {
    const chatService = new ChatService();
    const { name } = req.body;
    const status = await chatService.createChannel(name);
    res.status(status.code).json({ msg: status.msg });
  }

  public async joinChannel(req: Request, res: Response, _next: Function) {
    const chatService = new ChatService();
    console.log(req.body);
    const { username, names } = req.body;
    const status = await chatService.joinChannel(username, names);
    res.status(status.code).json({ msg: status.msg, channels: status.data });
  }

  public async getChannels(req: Request, res: Response, _next: Function) {
    const chatService = new ChatService();
    res.json(await chatService.getChannels());
  }

  public async getChannelHistory(req: Request, res: Response, _next: Function) {
    const chatService = new ChatService();
    const channelStatus = await chatService.getChannel(req.params.name);
    if (!channelStatus[0]) res.status(404).json({ msg: "Not Found" });
    res.json(channelStatus[1].chat);
  }
}

export const chatController = new ChatController();
