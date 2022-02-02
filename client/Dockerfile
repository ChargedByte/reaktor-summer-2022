FROM node:16-alpine

WORKDIR /app

RUN apk update && apk upgrade
RUN npm i -g --force npm@latest yarn@latest

COPY . .

RUN yarn install
RUN yarn build

ENV HOST=0.0.0.0
ENV PORT=3000

CMD ["yarn", "start"]
