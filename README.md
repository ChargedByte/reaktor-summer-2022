# Reaktor Summer 2022; Rock, Paper, Scissors (RPS)

My solution to Reaktor's Trainee Developer, summer
2022 [pre-assignment](https://web.archive.org/web/20220109201917/https://www.reaktor.com/assignment-2022-developers/).

At the moment this application is incomplete. The server is working and apart from adding some routes should be ready to
use. The client on the other hand is in early stages, it shouldn't take too long to get it working though.

## Features I intended to implement

- [ ] Testing
- [ ] Continuous integration
- [ ] Error handling (server only as some basic error handling, any unexpected behaviour could cause it to crash)
- [ ] Containerization (Docker/OCI), independently for server and client

## Server

The server works as a "cache" for the historical game data and serves the data via a REST API. The server is also
responsible for fetching the "massive" (~900k items at the time of writing) dataset from the Reaktor's API, and storing
it in a relation database.

The process of fetching the data from the Reaktor's API is relatively quick, it takes ~4 seconds to process a full page
of items. It's possible it could be sped up more. I'm fairly certain it's bottlenecked by the database connection and
better design there could help. Though once the whole dataset has been pulled (~1.5 hrs at the time of writing) it's
much faster to only add the new games to the database.

The server is written in [Kotlin](https://kotlinlang.org/).

Libraries used:

- [Guice](https://github.com/google/guice): Dependency injection
- [Ktor](https://github.com/ktorio/ktor): Web Server/Client framework
- [Netty](https://github.com/netty/netty): Asynchronous Web Server
- [Exposed](https://github.com/JetBrains/Exposed): ORM framework
- [HikariCP](https://github.com/brettwooldridge/HikariCP): Database connection pooling
- [Flyway](https://github.com/flyway/flyway): Database migrations

### Database

Theoretically you can use any database that has a JDBC driver but at least the ones
listed [here](https://github.com/JetBrains/Exposed#supported-databases) by Exposed are supported.

By default, the application uses a PostgreSQL database. You can opt to use something different by adding the driver to
the classpath.

### Notes

I opted to take some shortcuts as the application is not intended for production use.

- There is barely any error handling, this could change later.
- We are knowingly relying on the weak ETags provided by Reaktor's API.
- We are also expecting the Reaktor's API to also provide us with the rate limit headers.

## Client

The client is a server side rendering [Vue](https://vuejs.org/) application, responsible for displaying the game data
(WIP) and displaying the ongoing games vie Reaktor's WebSocket API (WIP).

The client is written in [TypeScript](https://www.typescriptlang.org/), [PostCSS](https://postcss.org/)
and [Vue](https://vuejs.org/) using [Nuxt](https://nuxtjs.org/).

Libraries used:

- [Vue](https://github.com/vuejs/vue): View layer
- [Nuxt](https://github.com/nuxt/nuxt.js): Framework for server side rendering of Vue
- [Vuetify](https://github.com/vuetifyjs/vuetify): Vuetify material components
- [ws](https://github.com/websockets/ws): WebSocket library
- [ESLint](https://github.com/eslint/eslint): Linter
- [Prettier](https://github.com/prettier/prettier): Code formatter

## License

This project is licensed under the [MIT license](LICENSE).
