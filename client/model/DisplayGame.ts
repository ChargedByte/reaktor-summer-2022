import ServerPlayer from "~/model/ServerPlayer";
import Hand from "~/model/Hand";
import ServerGame from "~/model/ServerGame";

class DisplayGame {
  constructor(game: ServerGame) {
    this._id = game.id;

    const epoch = game.playedAt;
    const playedAt = new Date(0);
    playedAt.setUTCSeconds(epoch);

    this._playedAt = playedAt;

    this._playerA = game.playerA;
    this._handA = game.handA;
    this._playerB = game.playerB;
    this._handB = game.handB;
    this._winner = game.winner;
  }

  private _id: String;

  get id(): String {
    return this._id;
  }

  set id(value: String) {
    this._id = value;
  }

  private _playedAt: Date;

  get playedAt(): Date {
    return this._playedAt;
  }

  set playedAt(value: Date) {
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

export default DisplayGame;
