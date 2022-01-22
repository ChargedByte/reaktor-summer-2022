<template>
  <v-app-bar app clipped-right color="primary" dark>
    <v-tabs>
      <v-tab>History</v-tab>
      <v-tab>Player</v-tab>
    </v-tabs>
  </v-app-bar>
</template>

<script lang="ts">
import { Component, Vue } from "nuxt-property-decorator";

@Component
export default class AppBar extends Vue {}
</script>
