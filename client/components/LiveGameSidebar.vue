<template>
  <v-navigation-drawer app clipped permanent right width="425">
    <v-list>
      <v-list-item v-for="game in games" :key="game.id">
        <LiveGame :game="game" />
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script lang="ts">
import { Component, Vue } from "nuxt-property-decorator";

import WebSocketGame from "~/model/WebSocketGame";
import WebSocketPlayer from "~/model/WebSocketPlayer";
import GameType from "~/model/GameType";
import Hand from "~/model/Hand";

@Component
export default class LiveGameSidebar extends Vue {
  games: WebSocketGame[] = [];

  ws: WebSocket | undefined;

  created() {
    if (typeof this.ws === "undefined") {
      const ws = new WebSocket("wss://bad-api-assignment.reaktor.com/rps/live");

      ws.onmessage = (event) => {
        const data = JSON.parse(
          event.data.replace(/\\"/g, '"').replace(/^"|"$/g, "")
        );

        const id = data.gameId;
        const type = data.type as GameType;

        const playerA = new WebSocketPlayer(data.playerA.name);
        const playerB = new WebSocketPlayer(data.playerB.name);

        const game = new WebSocketGame(id, type, playerA, playerB);

        if (type === GameType.GameResult) {
          const epoch = data.t;
          const time = new Date(0);
          time.setUTCMilliseconds(epoch);

          const handA = data.playerA.played as Hand;
          const handB = data.playerB.played as Hand;

          playerA.played = handA;
          playerB.played = handB;

          game.time = time;
        }

        let index = this.games.findIndex((g) => g.id === id);

        if (index !== -1) {
          this.games[index] = game;
        } else {
          this.games.push(game);
        }

        if (type === GameType.GameResult) {
          setTimeout(() => {
            index = this.games.findIndex((g) => g.id === id);
            this.games.splice(index, 1);
          }, 15 * 1000);
        }
      };

      this.ws = ws;
    }
  }

  beforeDestroy() {
    if (typeof this.ws !== "undefined") {
      this.ws.close(1000);
    }
  }
}
</script>

<style lang="scss" scoped>
.v-list-item:not(:first-child) {
  margin-top: 10px;
}
</style>
