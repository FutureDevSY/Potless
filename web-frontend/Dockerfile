FROM node:alpine as build-stage

ARG VITE_SERVICE_URL
ARG VITE_KAKAO_APP_KEY

ENV VITE_SERVICE_URL=${VITE_SERVICE_URL} \
    VITE_KAKAO_APP_KEY=${VITE_KAKAO_APP_KEY} \
    VITE_KAKAO_REST_API_KEY=${VITE_KAKAO_REST_API_KEY} \
    VITE_WEATHER_API_KEY=${VITE_WEATHER_API_KEY}

RUN echo "VITE_SERVICE_URL : $VITE_SERVICE_URL"
RUN echo "VITE_KAKAO_APP_KEY : $VITE_KAKAO_APP_KEY"
RUN echo "VITE_KAKAO_REST_API_KEY : $VITE_KAKAO_REST_API_KEY"
RUN echo "VITE_WEATHER_API_KEY : $VITE_WEATHER_API_KEY"

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine as production-stage

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

COPY --from=build-stage /app/dist /usr/share/nginx/html
