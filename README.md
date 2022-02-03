# Reaktor Summer 2022; Rock, Paper, Scissors (RPS)

My solution to Reaktor's Trainee Developer, summer
2022 [pre-assignment](https://web.archive.org/web/20220109201917/https://www.reaktor.com/assignment-2022-developers/).

## Server

The server works as a "cache" for the historical game data and serves the data via a REST API. The server is also
responsible for fetching the "massive" (~1M items at the time of writing) dataset from the Reaktor's API, and storing it
in a relation database.

The process of fetching the data from the Reaktor's API is quite quick, it takes about a second to fetch an entire page.

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

- The application can take sustains errors in the fetching process, errors aren't really handled just logged and the
  cursor is retried on the next go around.
- We are knowingly relying on the weak ETags provided by Reaktor's API.
- We are also expecting the Reaktor's API to also provide us with the rate limit headers.

## Client

The client is a [Vue](https://vuejs.org/) application, responsible for displaying the game amd player data and
displaying the ongoing games via Reaktor's WebSocket API.

The client is written in [TypeScript](https://www.typescriptlang.org/), [PostCSS](https://postcss.org/)
and [Vue](https://vuejs.org/) using [Nuxt](https://nuxtjs.org/).

Libraries used:

- [Vue](https://github.com/vuejs/vue): View layer
- [Nuxt](https://github.com/nuxt/nuxt.js): Framework for Vue
- [Vuetify](https://github.com/vuetifyjs/vuetify): Vuetify material components
- [ESLint](https://github.com/eslint/eslint): Linter
- [Prettier](https://github.com/prettier/prettier): Code formatter

## License

This project is licensed under the [MIT license](LICENSE).
