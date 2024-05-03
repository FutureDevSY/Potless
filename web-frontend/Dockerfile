FROM node:alpine as build-stage

ENV SERVICE_URL=${SERVICE_URL} \
    VITE_KAKAO_APP_KEY=${VITE_KAKAO_APP_KEY}

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine as production-stage

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

COPY --from=build-stage /app/dist /usr/share/nginx/html
