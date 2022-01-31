import ServerPlayer from "~/model/ServerPlayer";
import Hand from "~/model/Hand";

class ServerGame {
  constructor(
    id: string,
    playedAt: number,
    playerA: ServerPlayer,
    handA: Hand,
    playerB: ServerPlayer,
    handB: Hand,
    winner: ServerPlayer | null
  ) {
    this._id = id;
    this._playedAt = playedAt;
    this._playerA = playerA;
    this._handA = handA;
    this._playerB = playerB;
    this._handB = handB;
    this._winner = winner;
  }

  private _id: string;

  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  private _playedAt: number;

  get playedAt(): number {
    return this._playedAt;
  }

  set playedAt(value: number) {
    this._playedAt = value;
  }

  private _playerA: ServerPlayer;

  get playerA(): ServerPlayer {
    return this._playerA;
  }

  set playerA(value: ServerPlayer) {
    this._playerA = value;
  }

  private _handA: Hand;

  get handA(): Hand {
    return this._handA;
  }

  set handA(value: Hand) {
    this._handA = value;
  }

  private _playerB: ServerPlayer;

  get playerB(): ServerPlayer {
    return this._playerB;
  }

  set playerB(value: ServerPlayer) {
    this._playerB = value;
  }

  private _handB: Hand;

  get handB(): Hand {
    return this._handB;
  }

  set handB(value: Hand) {
    this._handB = value;
  }

  private _winner: ServerPlayer | null;

  get winner(): ServerPlayer | null {
    return this._winner;
  }

  set winner(value: ServerPlayer | null) {
    this._winner = value;
  }
}

export default ServerGame;
