import { ClassicGroupUtils } from "./classic/classic.group.utils";
import { IGroup, GameMode, IGroupPlayer, ISocketUser } from "../core/types";
import { CoopGroupUtils } from "./coop/coop.group.util";
import { TournamentGroupUtils } from "./tournament/tournament.group.utils";
import { TournamentRoundUtils } from "./tournament/tournament.round.utils";

export type UseGroupUtils =
  | ClassicGroupUtils
  | CoopGroupUtils
  | TournamentGroupUtils
  | TournamentRoundUtils;

export class GroupUtils {
  public static use(group: IGroup): UseGroupUtils {
    switch (group.mode) {
      case GameMode.CLASSIC:
        return new ClassicGroupUtils(group);

      case GameMode.COOP:
      case GameMode.SOLO:
        return new CoopGroupUtils(group);

      case GameMode.TOURNAMENT:
        return new TournamentGroupUtils(group);

      case GameMode.TOURNAMENT_ROUND:
        return new TournamentRoundUtils(group);

      default:
        return;
    }
  }

  public static sids(players: IGroupPlayer[]) {
    return players
      .filter(p => p.type === "REAL")
      .map(p => (p.user as ISocketUser).s.id);
  }
}
