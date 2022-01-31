<template>
  <v-chip :color="color" class="flex-fill mx-2 px-1 justify-center" label>
    <v-img
      v-if="player.played !== null"
      :src="src"
      height="24"
      width="24"
    ></v-img>
    <span class="ml-2">{{ player.name }}</span>
  </v-chip>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "nuxt-property-decorator";

import WebSocketPlayer from "~/model/WebSocketPlayer";
import Hand from "~/model/Hand";
import GameResult from "~/model/GameResult";

@Component
export default class LiveGamePlayer extends Vue {
  @Prop({ required: true })
  player!: WebSocketPlayer;

  @Prop()
  result: GameResult | undefined;

  get color() {
    switch (this.result) {
      case GameResult.Win:
        return "green";
      case GameResult.Lose:
        return "red";
      case GameResult.Draw:
        return "orange";
      default:
        return "";
    }
  }

  get src() {
    switch (this.player.played) {
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
}
</script>
