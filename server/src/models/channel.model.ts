import { Document, Model, model, Schema } from "mongoose";

export class IChatMessage {
  author: string = undefined;
  content: string = undefined;
  channel: string = undefined;
  created?: string = undefined;
}

export class IChannel {
  name: string = undefined;
  chat: IChatMessage[] = undefined;
}

export interface IChannelDocument extends IChannel, Document {}

export let ChatMessageSchema: Schema = new Schema({
  author: {
    type: Schema.Types.String,
    required: true,
  },
  content: {
    type: Schema.Types.String,
    required: true,
  },
  type: {
    type: Schema.Types.String,
    required: true,
  },
  created: {
    type: Schema.Types.String,
    required: true,
  },
});

export let ChannelSchema: Schema = new Schema({
  name: {
    type: Schema.Types.String,
    unique: true,
    required: true,
  },
  chat: {
    type: [Object],
    default: [],
  },
});

export const Channel: Model<IChannelDocument> = model<IChannelDocument>(
  "Channel",
  ChannelSchema
);
