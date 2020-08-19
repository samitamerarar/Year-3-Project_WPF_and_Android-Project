import { Express } from "express";
import { chatController } from "../controllers/chat.server.controller";

export default class ChatRoute {
  constructor(app: Express) {
    app.route("/channel").post(chatController.createChannel);
    app.route("/channel/join").post(chatController.joinChannel);
    app.route("/channels").get(chatController.getChannels);
    app.route("/channel/history/:name").get(chatController.getChannelHistory);
  }
}
