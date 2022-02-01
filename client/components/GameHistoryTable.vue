<template>
  <v-data-table
    :footer-props="footerProps"
    :headers="headers"
    :items="games"
    :loading="loading"
    :options.sync="options"
    :server-items-length="totalGames"
    disable-filtering
    disable-sort
  >
    <template #item.playedAt="{ item }">
      {{ item.playedAt.toISOString() }}
    </template>

    <template #item.playerA="{ item }">
      <GameHistoryPlayer :game="item" :is-player-a="true" />
    </template>

    <template #item.playerB="{ item }">
      <GameHistoryPlayer :game="item" :is-player-a="false" />
    </template>
  </v-data-table>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "nuxt-property-decorator";
import { DataOptions } from "vuetify";

import ServerPage from "~/model/ServerPage";
import DisplayGame from "~/model/DisplayGame";

@Component
export default class GameHistoryTable extends Vue {
  @Prop({ default: null })
  playerId!: number | null;

  loading = true;
  totalGames = 0;
  games: DisplayGame[] = [];

  options: DataOptions = {
    itemsPerPage: 10,
    page: 1,
    sortBy: [],
    sortDesc: [],
    groupBy: [],
    groupDesc: [],
    multiSort: false,
    mustSort: false,
  };

  footerProps = {
    itemsPerPageOptions: [5, 10, 15, 25, 50, 100, 150],
  };

  headers = [
    {
      text: "Id",
      value: "id",
      divider: true,
    },
    {
      text: "Time",
      value: "playedAt",
      divider: true,
    },
    {
      text: "Player A",
      value: "playerA",
      divider: true,
    },
    {
      text: "Player B",
      value: "playerB",
    },
  ];

  @Watch("options")
  onOptionsChanged() {
    this.$fetch();
  }

  async fetch() {
    this.loading = true;

    const { page, itemsPerPage } = this.options;

    let response: ServerPage;

    if (this.playerId === null) {
      response = await this.$http.$get(
        `/v1/games?size=${itemsPerPage}&page=${page - 1}`
      );
    } else {
      response = await this.$http.$get(
        `/v1/player/${this.playerId}/games?size=${itemsPerPage}&page=${
          page - 1
        }`
      );
    }

    this.totalGames = response.totalPages * itemsPerPage;
    this.games = response.games.flatMap((g) => new DisplayGame(g));

    this.loading = false;
  }
}
</script>
