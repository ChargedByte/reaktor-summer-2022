<template>
  <v-container v-if="player && stats">
    <v-row no-gutters>
      <v-card class="flex-fill" flat>
        <v-card-title>{{ player.name }}</v-card-title>
        <v-card-text>
          <v-row no-gutters>
            <v-col>
              <div class="font-weight-bold">Total Games:</div>
              {{ stats.total }}
            </v-col>
            <v-col>
              <div class="font-weight-bold">Games Won:</div>
              {{ stats.wins }}
            </v-col>
            <v-col>
              <div class="font-weight-bold">Games Lost:</div>
              {{ stats.total - stats.wins }}
            </v-col>
            <v-col>
              <div class="font-weight-bold">Win Rate:</div>
              {{ (stats.winRatio * 100).toFixed(2) }}%
            </v-col>
            <v-col>
              <div class="font-weight-bold">Most Played Hand:</div>
              <v-img :src="src" height="48" width="48"></v-img>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-row>
    <v-divider></v-divider>
    <v-row no-gutters>
      <v-col>
        <GameHistoryTable :player-id="player.id" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from "nuxt-property-decorator";

import GameHistoryTable from "~/components/GameHistoryTable.vue";
import ServerPlayer from "~/model/ServerPlayer";
import PlayerStats from "~/model/PlayerStats";
import Hand from "~/model/Hand";

@Component({
  components: { GameHistoryTable },
})
export default class PlayerIdPage extends Vue {
  player: ServerPlayer | null = null;
  stats: PlayerStats | null = null;

  get src() {
    switch (this.stats?.mostPlayed) {
      case Hand.Rock:
        return "https://img.icons8.com/color/50/000000/rock.png";
      case Hand.Paper:
        return "https://img.icons8.com/color/50/000000/paper.png";
      case Hand.Scissors:
        return "https://img.icons8.com/color/50/000000/sewing-scissors.png";
      default:
        return "";
    }
  }

  async fetch() {
    const id = Number(this.$route.params.id);

    if (isNaN(id)) {
      this.$nuxt.error({ statusCode: 400, message: "Invalid player id" });
      return;
    }

    let response;
    try {
      response = await this.$http.get(`/v1/player/${id}`);
    } catch (_) {
      this.$nuxt.error({ statusCode: 404, message: "Player not found" });
      return;
    }

    this.player = await response.json();

    this.stats = await this.$http.$get(`/v1/player/${id}/stats`);
  }
}
</script>
