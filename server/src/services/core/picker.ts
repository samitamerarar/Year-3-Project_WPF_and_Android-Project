export function pick<K, T extends K>(K: new () => K, obj: T): K {
  const copy = {} as K;
  Object.keys(new K()).forEach(key => (copy[key] = obj[key]));
  return copy;
}
