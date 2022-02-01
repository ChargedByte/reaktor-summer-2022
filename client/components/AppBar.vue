<template>
  <v-app-bar app clipped-right color="primary" dark>
    <v-tabs>
      <v-tab to="/">History</v-tab>
      <v-tab to="/player">Player</v-tab>
    </v-tabs>

    <v-app-bar-nav-icon
      class="hidden-lg-and-up"
      @click.native="openLiveGameDrawer()"
    ></v-app-bar-nav-icon>
  </v-app-bar>
</template>

<script lang="ts">
import { Component, Vue } from "nuxt-property-decorator";

import { generalStore } from "~/utils/store-accessor";

@Component
export default class AppBar extends Vue {
  openLiveGameDrawer() {
    generalStore.setLiveGameDrawer(true);
  }
}
</script>
