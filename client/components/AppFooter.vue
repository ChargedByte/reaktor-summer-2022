<template>
  <v-footer app>
    <span
      >Icons
      <a href="https://icons8.com/icon/16463/rock" target="_blank">Rock</a>,
      <a href="https://icons8.com/icon/13580/paper" target="_blank">Paper</a>,
      <a href="https://icons8.com/icon/85716/cutting" target="_blank"
        >Cutting</a
      >
      by <a href="https://icons8.com" target="_blank">Icons8</a></span
    >

    <v-spacer></v-spacer>

    <v-btn
      href="https://github.com/ChargedByte/reaktor-summer-2022"
      icon
      small
      target="_blank"
    >
      <v-icon>mdi-github</v-icon>
    </v-btn>
  </v-footer>
</template>

<script>
import { Component, Vue } from "nuxt-property-decorator";

@Component
export default class AppFooter extends Vue {}
</script>
