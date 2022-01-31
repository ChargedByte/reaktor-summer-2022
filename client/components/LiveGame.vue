<template>
  <div class="live-game">
    <header>
      {{ game.id }}
    </header>

    <div>
      <LiveGamePlayer :player="game.playerA" :result="playerAResult" />
      <div>vs</div>
      <LiveGamePlayer :player="game.playerB" :result="playerBResult" />
    </div>

    <footer v-if="game.time !== null" class="flex flex-row">
      <span>{{ game.time.toISOString() }}</span>
    </footer>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "nuxt-property-decorator";

import WebSocketGame from "~/model/WebSocketGame";
import LiveGamePlayer from "~/components/LiveGamePlayer.vue";
import { handBeatsHand } from "~/model/Hand";
import GameResult from "~/model/GameResult";

@Component({
  components: { LiveGamePlayer },
})
export default class LiveGame extends Vue {
  @Prop({ required: true })
  game!: WebSocketGame;

  get playerAResult() {
    const { playerA, playerB } = this.game;

    if (playerA.played === null || playerB.played === null) {
      return null;
    }

    if (handBeatsHand(playerA.played, playerB.played)) {
      return GameResult.Win;
    } else if (handBeatsHand(playerB.played, playerA.played)) {
      return GameResult.Lose;
    } else {
      return GameResult.Draw;
    }
  }

  get playerBResult() {
    const { playerA, playerB } = this.game;

    if (playerA.played === null || playerB.played === null) {
      return null;
    }

    if (handBeatsHand(playerB.played, playerA.played)) {
      return GameResult.Win;
    } else if (handBeatsHand(playerA.played, playerB.played)) {
      return GameResult.Lose;
    } else {
      return GameResult.Draw;
    }
  }
}
</script>

<style lang="scss" scoped>
.live-game {
  width: 100%;
  display: flex;
  flex-direction: column;
  padding: 0.5rem;
  box-shadow: 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 2px 2px 0 rgba(0, 0, 0, 0.14),
    0 1px 5px 0 rgba(0, 0, 0, 0.12) !important;
  border-radius: 3px;

  header,
  footer {
    font-size: 0.75rem;
    color: #959595;
  }

  div {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 5px 0;
  }
}
</style>
