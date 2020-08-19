export class Utils {
  public static rand(min: number, max: number) {
    return Math.floor(Math.random() * (max - min)) + min;
  }

  public static filterStringify(obj: any, key: string) {
    return JSON.stringify(obj, (k, v) => {
      if (k === key) return;
      else return v;
    });
  }

  public static any<T>(arr: Array<T>) {
    return arr[Utils.rand(0, arr.length - 1)];
  }

  public static isChar(s: string) {
    return s.length === 1 && /^[a-zA-Z]+$/.test(s);
  }

  public static clone<T>(arg: T): T {
    return JSON.parse(JSON.stringify(arg));
  }
}
