import { Document, Model, model, Schema } from "mongoose";

export interface IUserTutorials {
  classic: boolean;
  solo: boolean;
  coop: boolean;
  tournament: boolean;
}

export class IUser {
  username: string = undefined;
  password: string = undefined;
  channels?: string[] = undefined;
  created?: Date = undefined;
  firstName?: string = undefined;
  lastName?: string = undefined;
  avatar?: string = undefined;
  loginTimes?: string[] = undefined;
  logoutTimes?: string[] = undefined;
  connected?: boolean = undefined;
  tutorials?: IUserTutorials = undefined;
}

export interface IUserDocument extends IUser, Document {}

export let UserSchema: Schema = new Schema({
  username: {
    type: Schema.Types.String,
    unique: true,
    required: true,
  },
  password: {
    type: Schema.Types.String,
    required: true,
  },
  channels: {
    type: [String],
    default: ["general", "game"],
  },
  created: {
    type: Schema.Types.String,
    default: Date.now(),
  },
  firstName: {
    type: Schema.Types.String,
  },
  lastName: {
    type: Schema.Types.String,
  },
  avatar: {
    type: Schema.Types.String,
  },
  loginTimes: {
    type: [String],
    default: [new Date(Date.now()).toLocaleString()],
  },
  logoutTimes: {
    type: [String],
    default: [],
  },
  connected: {
    type: Schema.Types.Boolean,
    default: true,
  },
  tutorials: {
    type: Schema.Types.Mixed,
    default: {
      classic: true,
      solo: true,
      coop: true,
      tournament: true,
    },
  },
});

export const User: Model<IUserDocument> = model<IUserDocument>(
  "User",
  UserSchema
);
