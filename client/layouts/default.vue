<template>
  <v-app id="reaktor-summer-2022">
    <LiveGameSidebar />

    <AppBar />

    <v-main>
      <Nuxt />
    </v-main>

    <AppFooter />
  </v-app>
</template>

<script lang="ts">
import { Component, Vue } from "nuxt-property-decorator";

@Component
export default class DefaultLayout extends Vue {}
</script>
