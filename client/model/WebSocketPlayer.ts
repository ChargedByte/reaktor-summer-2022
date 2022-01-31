import Hand from "~/model/Hand";

class WebSocketPlayer {
  constructor(name: string) {
    this._name = name;
    this._played = null;
  }

  private _name: string;

  get name(): string {
    return this._name;
  }

  set name(value: string) {
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
