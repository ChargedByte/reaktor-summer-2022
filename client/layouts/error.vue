<template>
  <v-container class="container-error pa-0" fill-height fluid>
    <v-row align="center" justify="center">
      <v-card class="transparent" dark flat>
        <v-row align="center" justify="center">
          <span class="text-status">Error {{ status }}</span>
        </v-row>

        <v-row align="center" justify="center">
          <span class="text-message">{{ message }}</span>
        </v-row>

        <v-row align="center" class="mt-10" justify="center">
          <v-btn to="/player">Go back</v-btn>
        </v-row>
      </v-card>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "nuxt-property-decorator";

@Component
export default class ErrorPage extends Vue {
  @Prop()
  error!: any;

  get status() {
    return this.error.statusCode;
  }

  get message() {
    if (this.error.message) {
      return this.error.message;
    }

    switch (this.status) {
      case 404:
        return "Page Not Found";
      default:
        return "An Error Occurred";
    }
  }
}
</script>

<style lang="scss" scoped>
.container-error {
  background-color: #005eb6;
}

.text-status {
  font-size: 10rem;
  font-weight: lighter;
}

.text-message {
  font-size: 2.5rem;
  font-weight: lighter;
}
</style>
