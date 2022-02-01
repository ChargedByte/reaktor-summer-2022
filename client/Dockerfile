FROM node:16-alpine AS base

WORKDIR /app

RUN apk update && apk upgrade

RUN npm i -g --force npm@latest yarn@latest

FROM base AS build

COPY . .

RUN yarn install
RUN yarn build

FROM base AS deploy

EXPOSE 3000

ENV NODE_ENV=production

COPY --from=build ["/app/.nuxt", "/app/dist", "/app/nuxt.config.ts", "/app/package.json", "/app/yarn.lock", "./"]

RUN yarn install --production

CMD ["yarn", "start"]
