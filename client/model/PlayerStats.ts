import Hand from "~/model/Hand";

type PlayerStats = {
  wins: number;
  total: number;
  winRatio: number;
  mostPlayed: Hand;
};

export default PlayerStats;
