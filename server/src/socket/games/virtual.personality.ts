import { Utils } from "../../utils/utils";

const congratulations = [
  `Félicitation %username! Tu as deviné "%guess" et c'était la bonne réponse!`,
  `Bonne réponse %username! Effectivement c'était "%guess"`,
  `Très bien réfléchi %username! "%guess" est la bonne réponse!`,
  `Mais t'es un pro %username! Comment a- tu pu deviner "%guess"!?`,
  `Les astres sont surment allignés!! %username a eu la bonne réponse : "%guess"!!!!`,
  `Ta sagesse est d'une grande renommé %username, "%guess" est la bonne réponse!`,
];

const encouragement = [
  `Bonne tentative %username! Malheureusement, "%guess" n'est pas la bonne réponse.`,
  `Wow, %username, bien réfléchi, mais "%guess" n'était pas la bonne réponse...`,
  `Hmmm "%guess", bien essayé %username, mais essaye encore!`,
  `"%guess" n'est pas la bonne réponse %username, tu peux demander un indice!!`,
  `Tant pis, "%guess" n'est pas la bonne réponse, que la force soit avec toi %username!!`,
];

export class VirtualPersonality {
  public static congratulate(username: string, guess: string) {
    return this.replace(Utils.any(congratulations), username, guess);
  }

  public static encourage(username: string, guess: string) {
    return this.replace(Utils.any(encouragement), username, guess);
  }

  private static replace(s: string, username: string, guess: string) {
    return s.replace(/%username/g, username).replace(/%guess/g, guess);
  }
}
