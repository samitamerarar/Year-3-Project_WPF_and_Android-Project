import { Request, Response } from "express";

export default class IndexController {
  public index(req: Request, res: Response, next: Function): void {
    res.render("index", { title: "Projet 3 - Équipe 06" });
  }

  public msg(req: Request, res: Response): void {
    res.json({ msg: "Hello!!" });
  }
}

export const indexController = new IndexController();
