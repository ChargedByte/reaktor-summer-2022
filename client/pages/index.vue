<template>
  <v-container fluid>
    <GameHistoryTable />
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from "nuxt-property-decorator";

@Component
export default class IndexPage extends Vue {}
</script>
