import { ServiceStatus, IImageStructre } from "./core/interfaces";
import { Scraper } from "simple-html-scraper";
import * as fs from "fs";
import * as uid from "uniqid";
import { PotraceService, PotraceOptions } from "./potrace.server.service";

type ImageStatus = ServiceStatus<null>;

const GOOGLE_IMAGE_URL = (keyword: string) =>
  `https://www.google.com/search?tbm=isch&q=${keyword}&tbs=itp:lineart`;

const CUSTOM_ARGS = [
  // Required for Docker version of Puppeteer
  "--no-sandbox",
  "--disable-setuid-sandbox",
  // This will write shared memory files into /tmp instead of /dev/shm,
  // because Docker’s default for /dev/shm is 64MB
  "--disable-dev-shm-usage",
];

export class ImageService {
  public async search(keyword: string): Promise<string[]> {
    const options = { puppeteer: { args: CUSTOM_ARGS } };
    const scraper = new Scraper(options);
    const result = await scraper.get(GOOGLE_IMAGE_URL(keyword));
    const images = result.images
      .filter(im => im.startsWith("data:image/jpeg"))
      .reverse();

    return images;
  }

  public fileExists(path: string) {
    return fs.existsSync(path);
  }

  public getImageExtension(path: string) {
    return path.split(".")[1];
  }

  public get(path: string) {
    return fs.readFileSync(path);
  }

  public async chooseImage(url: string, options?: PotraceOptions) {
    const id = uid();
    const original = `image_${id}.jpg`;
    const svg = `svg_${id}.svg`;

    const path = `images/${original}`;
    this.downloadImage(url, path);

    await PotraceService.trace(path, `images/${svg}`, options);

    return { id: svg };
  }

  public async upload(binary: any) {
    const [, name] = this._upload(binary);
    return name;
  }

  public async convert(binary: any, options?: PotraceOptions) {
    const [id, name] = this._upload(binary);
    const svg = `svg_${id}.svg`;

    await PotraceService.trace(`images/${name}`, `images/${svg}`, options);

    return svg;
  }

  public saveImageData(data: IImageStructre) {
    const id = uid();
    const image = `image_${id}.json`;
    fs.writeFileSync(`images/${image}`, JSON.stringify(data));

    return image;
  }

  private _upload(binary: any) {
    const id = uid();
    const name = `image_${id}.any`;
    const path = `images/${name}`;
    fs.writeFileSync(path, binary, "base64");

    return [id, name];
  }

  private downloadImage(url: string, path: string) {
    const base64Data = url.replace(/^data:image\/jpeg;base64,/, "");
    fs.writeFileSync(path, base64Data, "base64");
  }
}
