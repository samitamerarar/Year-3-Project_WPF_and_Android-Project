import { Request, Response } from "express";
import { AuthService } from "../services/auth.server.service";

export default class AuthController {
  public async auth(req: Request, res: Response, _next: Function) {
    const authService = new AuthService();

    if (!authService.validate(req.body)) {
      res.status(400).json({ msg: "Malformed Request" });
    }

    const { username, password } = req.body;
    const status = await authService.login(username, password);

    if (status.code !== 404) {
      res.status(status.code).json({ msg: status.msg, user: status.data });
    } else {
      const user = await authService.register(username, password);
      res.status(200).json({ msg: "Account Created!", user });
    }
  }

  public async updateProfile(req: Request, res: Response, _next: Function) {
    const username = req.params.username;
    const { firstName, lastName, avatar } = req.body;

    const service = new AuthService();
    const u = { firstName, lastName, avatar };
    const r = await service.updateProfile(username, u);

    res.status(r.code).json({ msg: r.msg });
  }

  public async getProfile(req: Request, res: Response, _next: Function) {
    const username = req.params.username;
    const service = new AuthService();
    const { code, msg, data } = await service.getProfile(username);

    res.status(code).json({ msg, data });
  }

  public async tutorial(req: Request, res: Response, _next: Function) {
    const { tutorial, username } = req.params;

    const service = new AuthService();
    const { code, msg } = await service.tutorial(username, tutorial);

    res.status(code).json({ msg });
  }

  public async deleteUser(req: Request, res: Response, _next: Function) {
    const { username } = req.params;

    const service = new AuthService();
    const { code, msg } = await service.deleteUser(username);

    res.status(code).json({ msg });
  }
}

export const authController = new AuthController();
