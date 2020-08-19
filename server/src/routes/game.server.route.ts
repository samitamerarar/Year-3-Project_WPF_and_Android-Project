import { Express } from "express";
import { gameController } from "../controllers/game.server.controller";

export default class GameRoute {
  constructor(app: Express) {
    app.route("/game").post(gameController.createGame);
  }
}
