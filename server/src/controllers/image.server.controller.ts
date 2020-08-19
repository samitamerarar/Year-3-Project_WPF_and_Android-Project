import { Request, Response } from "express";
import { ImageService } from "../services/image.server.service";
import { IImageStructre } from "services/core/interfaces";

export default class ImageController {
  public async search(req: Request, res: Response, _next: Function) {
    const keyword = req.params.keyword;
    const images = await new ImageService().search(keyword);
    res.json(images);
  }

  public async get(req: Request, res: Response, _next: Function) {
    const path = `images/${req.params.id}`;
    const service = new ImageService();

    if (!service.fileExists(path)) {
      res.status(404).json("FILE NOT FOUND");
      return;
    }

    res.writeHead(200, {
      "Content-Type": "image/" + service.getImageExtension(path),
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Headers":
        "Origin, X-Requested-With, Content-Type, Accept, Referer, Range, Accept-Encoding",
    });

    res.end(service.get(path), "binary");
  }

  public async choose(req: Request, res: Response, _next: Function) {
    const { url } = req.body;

    const potraceOptions = req.query.potrace
      ? JSON.parse(req.query.potrace)
      : undefined;

    const id = await new ImageService().chooseImage(url, potraceOptions);
    res.json(id);
  }

  public async upload(req: Request, res: Response, _next: Function) {
    let id: string;

    try {
      id = await new ImageService().upload(req.body.data);
    } catch (e) {
      res.status(400).json({ error: "Malformed Request" });
    }

    res.json({ id });
  }

  public async convert(req: Request, res: Response, _next: Function) {
    let id: string;

    const potraceOptions = req.query.potrace
      ? JSON.parse(req.query.potrace)
      : undefined;

    try {
      id = await new ImageService().convert(req.body.data, potraceOptions);
    } catch (e) {
      res.status(400).json({ error: "Malformed Request" });
    }

    res.json({ id });
  }

  public fromData(req: Request, res: Response, _next: Function) {
    const data: IImageStructre = req.body;
    const id = new ImageService().saveImageData(data);

    res.json({ id });
  }
}

export const imageController = new ImageController();
