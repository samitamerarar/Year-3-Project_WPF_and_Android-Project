import { Document, Model, model, Schema } from "mongoose";

export enum GameDifficulty {
  EASY = "EASY",
  INTERMEDIATE = "INTERMEDIATE",
  DIFFICULT = "DIFFICULT",
}

export enum DrawingMode {
  NORMAL = "NORMAL",
  RANDOM = "RANDOM",
  PANORAMIC_L = "PANORAMIC_L",
  PANORAMIC_R = "PANORAMIC_R",
  PANORAMIC_U = "PANORAMIC_U",
  PANORAMIC_D = "PANORAMIC_D",
  CENTERED_IN = "CENTERED_IN",
  CENTERED_OUT = "CENTERED_OUT",
}

export class IGame {
  name: string = undefined;
  answer: string = undefined;
  clues: string[] = undefined;
  image: string = undefined;
  difficulty: GameDifficulty = undefined;
  created: Date = undefined;
  drawingMode: DrawingMode = undefined;
}

export interface IGameDocument extends IGame, Document {}

export let GameSchema: Schema = new Schema({
  name: {
    type: Schema.Types.String,
    unique: true,
    required: true,
  },
  answer: {
    type: Schema.Types.String,
    required: true,
  },
  clues: {
    type: [String],
    default: [],
  },
  image: {
    type: Schema.Types.String,
    required: true,
  },
  difficulty: {
    type: Schema.Types.String,
    required: true,
  },
  created: {
    type: Schema.Types.String,
    default: Date.now(),
  },
  drawingMode: {
    type: Schema.Types.String,
    required: true,
  },
});

export const Game: Model<IGameDocument> = model<IGameDocument>(
  "Game",
  GameSchema
);
