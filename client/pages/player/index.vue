<template>
  <v-container>
    <v-row no-gutters>
      <v-col>
        <v-text-field v-model="query" filled label="Search"></v-text-field>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-col>
        <v-list>
          <v-list-item v-for="player in players" :key="player.id">
            <NuxtLink :to="'/player/' + player.id">{{ player.name }}</NuxtLink>
          </v-list-item>
        </v-list>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "nuxt-property-decorator";

import ServerPlayer from "~/model/ServerPlayer";

@Component
export default class PlayerSearchPage extends Vue {
  players: ServerPlayer[] = [];

  query: string | null = null;

  @Watch("query")
  onQueryChanged() {
    this.$fetch();
  }

  async fetch() {
    if (this.query === null || this.query === "") return;

    this.players = await this.$http.$get(
      `/v1/player/search/${encodeURI(this.query)}`
    );
  }
}
</script>
