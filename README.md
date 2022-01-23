# Reaktor Summer 2022; Rock, Paper, Scissors (RPS)

My solution to Reaktor's Trainee Developer, summer
2022 [pre-assignment](https://web.archive.org/web/20220109201917/https://www.reaktor.com/assignment-2022-developers/).

At the moment this application is incomplete, and even though the application period has ended I intend to finish it.

## Server

The server works as a "cache" for the historical game data and serves the data via a REST API. The server also is
responsible for fetching the "massive" (~900k items at the time of writing) dataset from the Reaktor's API, and storing
it in a relation database.

The server is written in [Kotlin](https://kotlinlang.org/).

Libraries used:

- [Ktor](https://github.com/ktorio/ktor): Web Server/Client framework
- [Netty](https://github.com/netty/netty): Asynchronous Web Server
- [Exposed](https://github.com/JetBrains/Exposed): ORM framework
- [HikariCP](https://github.com/brettwooldridge/HikariCP): Database connection pooling
- [Flyway](https://github.com/flyway/flyway): Database migrations

## Client

The client is a server side rendering [Vue](https://vuejs.org/) application, responsible for displaying the game data (
WIP) and displaying the ongoing games vie Reaktor's WebSocket API (WIP).

The client is written in [TypeScript](https://www.typescriptlang.org/) and [Vue](https://vuejs.org/)
using [Nuxt](https://nuxtjs.org/).

Libraries used:

- [Vue](https://github.com/vuejs/vue): View layer
- [Nuxt](https://github.com/nuxt/nuxt.js): Framework for server side rendering of Vue
- [Vuetify](https://github.com/vuetifyjs/vuetify): Vuetify material components
- [ws](https://github.com/websockets/ws): WebSocket library
- [ESLint](https://github.com/eslint/eslint): Linter
- [Prettier](https://github.com/prettier/prettier): Code formatter

## License

This project is licensed under the [MIT license](LICENSE).
