import { Express } from "express";
import { authController } from "../controllers/auth.server.controller";

export default class AuthRoute {
  constructor(app: Express) {
    app.route("/auth").post(authController.auth);
    app.route("/user/profile/:username").post(authController.updateProfile);
    app.route("/user/profile/:username").get(authController.getProfile);
    app.route("/tutorial/:username/:tutorial").post(authController.tutorial);
    app.route("/user/delete/:username").post(authController.deleteUser);
  }
}
