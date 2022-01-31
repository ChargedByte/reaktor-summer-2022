import { NuxtConfig } from "@nuxt/types";

const config: NuxtConfig = {
  // Server-side rendering: https://go.nuxtjs.dev/config-ssr
  ssr: false,

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: "%s - reaktor-summer-2022",
    title: "RPS",
    htmlAttrs: { lang: "en" },
    meta: [
      { charset: "utf-8" },
      {
        name: "viewport",
        content: "width=device-width, initial-scale=1",
      },
      { name: "format-detection", content: "telephone=no" },
    ],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    "@nuxt/typescript-build", // https://go.nuxtjs.dev/typescript
    "@nuxtjs/vuetify", // https://go.nuxtjs.dev/vuetify
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    "@nuxt/http", // https://http.nuxtjs.org/
  ],

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    customVariables: ["~/assets/variables.scss"],
    theme: {},
  },

  // Http module configuration: https://http.nuxtjs.org/options
  http: {},

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {},
};

export default config;
