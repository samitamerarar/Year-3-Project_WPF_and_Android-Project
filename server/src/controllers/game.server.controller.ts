import { Request, Response } from "express";
import { GameService } from "../services/game.server.service";

export default class GameController {
  public async createGame(req: Request, res: Response, _next: Function) {
    const form = req.body;
    const status = await new GameService().createGame(form);
    res.status(status.code).json({ msg: status.msg });
  }
}

export const gameController = new GameController();
