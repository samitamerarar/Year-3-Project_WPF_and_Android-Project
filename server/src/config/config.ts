import { sync } from "glob";
import { union } from "lodash";

export default class Config {
  public static port: string = process.env.PORT || "5050";
  public static routes: string = "./dist/routes/**/*.js";
  public static models: string = "./dist/models/**/*.js";
  public static useMongo: boolean = true;
  public static mongodb =
    "mongodb+srv://admin:DnjtV35GeUs6H1EK@cluster0-yltcq.gcp.mongodb.net/database?retryWrites=true&w=majority";
  public static globFiles(location: string): string[] {
    return union([], sync(location));
  }
}
