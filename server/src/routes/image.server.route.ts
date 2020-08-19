import { Express } from "express";
import { imageController } from "../controllers/image.server.controller";

export default class ImageRoute {
  constructor(app: Express) {
    app.route("/image/search/:keyword").get(imageController.search);
    app.route("/image/:id").get(imageController.get);
    app.route("/image/choose").post(imageController.choose);
    app.route("/image/upload").post(imageController.upload);
    app.route("/image/convert").post(imageController.convert);
    app.route("/image/data").post(imageController.fromData);
  }
}
