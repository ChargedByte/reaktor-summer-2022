import Hand from "~/model/Hand";

class WebSocketPlayer {
  constructor(name: String) {
    this._name = name;
    this._played = null;
  }

  private _name: String;

  get name(): String {
    return this._name;
  }

  set name(value: String) {
    this._name = value;
  }

  private _played: Hand | null;

  get played(): Hand | null {
    return this._played;
  }

  set played(value: Hand | null) {
    this._played = value;
  }
}

export default WebSocketPlayer;
