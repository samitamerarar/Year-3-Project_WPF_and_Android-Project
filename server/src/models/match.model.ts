import { Document, Model, model, Schema } from "mongoose";

export enum GameMode {
  CLASSIC = "CLASSIC",
  SOLO = "SOLO",
  COOP = "COOP",
  TOURNAMENT = "TOURNAMENT",
  TOURNAMENT_ROUND = "TOURNAMENT_ROUND",
}

export class IMatch {
  start: number = undefined;
  end: number = undefined;
  duration: number = undefined;
  participants: string[] = undefined;
  mode: GameMode = undefined;
  resultClassic?: { w: string[]; l: string[] } = undefined;
  resultCoop?: number = undefined;
}

export interface IMatchDocument extends IMatch, Document {}

export let MatchSchema: Schema = new Schema({
  start: {
    type: Schema.Types.Number,
    required: true,
  },
  end: {
    type: Schema.Types.Number,
    required: true,
  },
  duration: {
    type: Schema.Types.Number,
    required: true,
  },
  participants: {
    type: [String],
    required: true,
  },
  mode: {
    type: Schema.Types.String,
    required: true,
  },
  resultClassic: {
    type: Schema.Types.Mixed,
  },
  resultCoop: {
    type: Schema.Types.Number,
  },
});

export const Match: Model<IMatchDocument> = model<IMatchDocument>(
  "Match",
  MatchSchema
);
