FROM node:alpine AS client-build

WORKDIR /build/

COPY client/package.json /build/
RUN npm install
COPY client/ /build/
RUN npm run build

FROM maven:3-eclipse-temurin-21-alpine AS server-build

WORKDIR /build/

COPY pom.xml /build/
COPY src /build/src
RUN --mount=type=cache,target=/root/.m2 mvn compile package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app/
RUN apk add --update --no-cache nginx
COPY docker-fs/ /
COPY --from=client-build /build/dist/ /app/static/
COPY --from=server-build /build/target/*.jar /app/target/

ENTRYPOINT ["/entrypoint.sh"]
