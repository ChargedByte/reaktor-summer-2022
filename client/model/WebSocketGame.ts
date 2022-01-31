import GameType from "~/model/GameType";
import WebSocketPlayer from "~/model/WebSocketPlayer";

class WebSocketGame {
  constructor(
    id: String,
    type: GameType,
    playerA: WebSocketPlayer,
    playerB: WebSocketPlayer
  ) {
    this._id = id;
    this._type = type;
    this._time = null;
    this._playerA = playerA;
    this._playerB = playerB;
  }

  private _id: String;

  get id(): String {
    return this._id;
  }

  set id(value: String) {
    this._id = value;
  }

  private _type: GameType;

  get type(): GameType {
    return this._type;
  }

  set type(value: GameType) {
    this._type = value;
  }

  private _time: Date | null;

  get time(): Date | null {
    return this._time;
  }

  set time(value: Date | null) {
    this._time = value;
  }

  private _playerA: WebSocketPlayer;

  get playerA(): WebSocketPlayer {
    return this._playerA;
  }

  set playerA(value: WebSocketPlayer) {
    this._playerA = value;
  }

  private _playerB: WebSocketPlayer;

  get playerB(): WebSocketPlayer {
    return this._playerB;
  }

  set playerB(value: WebSocketPlayer) {
    this._playerB = value;
  }
}

export default WebSocketGame;
