export interface ServiceStatus<D> {
  code: number;
  msg: string;
  data: D | null;
}

export interface IImagePoint {
  x: number;
  y: number;
}

export interface IImageSegment {
  pointNature: "ellipse" | "rectangle";
  color?: string;
  width?: number;
  height?: number;
  points: IImagePoint[];
}

export interface IImageStructre {
  segments: IImageSegment[];
}
