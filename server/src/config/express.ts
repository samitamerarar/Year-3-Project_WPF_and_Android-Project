import * as bodyParser from "body-parser";
import * as cookieParser from "cookie-parser";
import * as express from "express";
import * as mongoose from "mongoose";
import * as logger from "morgan";
import * as path from "path";
import config from "./config";

export default function() {
  const app: express.Express = express();

  for (const model of config.globFiles(config.models)) {
    require(path.resolve(model));
  }

  if (config.useMongo) {
    mongoose.set("useNewUrlParser", true);
    mongoose.set("useFindAndModify", false);
    mongoose.set("useCreateIndex", true);
    mongoose.set("useUnifiedTopology", true);
    mongoose.connect(config.mongodb).catch(() => {
      console.log("Error connecting to mongo");
    });
  }

  app.set("views", path.join(__dirname, "../../src/views"));
  app.set("view engine", "pug");

  app.use(logger("dev"));
  app.use(bodyParser.json({ limit: "100mb" }));
  app.use(bodyParser.urlencoded({ extended: false }));

  app.use(
    bodyParser.raw({
      inflate: true,
      limit: "100mb",
      type: "application/octet-stream",
    })
  );

  app.use(cookieParser());
  app.use(express.static(path.join(__dirname, "../../src/public")));

  for (const route of config.globFiles(config.routes)) {
    require(path.resolve(route)).default(app);
  }

  app.use(
    (req: express.Request, res: express.Response, next: Function): void => {
      const err: Error = new Error("Not Found");
      next(err);
    }
  );

  return app;
}
