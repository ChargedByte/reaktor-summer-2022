<template>
  <v-chip
    :color="color"
    class="justify-center"
    label
    @click="$router.push({ path: `/player/${player.id}` })"
  >
    <span>{{ player.name }}</span>
    <v-img :src="src" class="ml-2" height="32" width="32"></v-img>
  </v-chip>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "nuxt-property-decorator";

import DisplayGame from "~/model/DisplayGame";
import Hand from "~/model/Hand";
import GameResult from "~/model/GameResult";

@Component
export default class GameHistoryPlayer extends Vue {
  @Prop({ required: true })
  game!: DisplayGame;

  @Prop({ default: false })
  isPlayerA!: boolean;

  get player() {
    return this.isPlayerA ? this.game.playerA : this.game.playerB;
  }

  get hand() {
    return this.isPlayerA ? this.game.handA : this.game.handB;
  }

  get result() {
    if (this.game.winner) {
      if (this.game.winner.id === this.player.id) {
        return GameResult.Win;
      } else {
        return GameResult.Lose;
      }
    }
    return GameResult.Draw;
  }

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
    switch (this.hand) {
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
