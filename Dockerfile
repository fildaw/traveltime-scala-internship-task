ARG ECLIPSE_TEMURIN_VERSION="24.0.1_9"
ARG SBT_VERSION="1.11.5"
ARG SCALA_VERSION="2.13.16"

FROM sbtscala/scala-sbt:eclipse-temurin-${ECLIPSE_TEMURIN_VERSION}_${SBT_VERSION}_${SCALA_VERSION} AS builder
WORKDIR /build
COPY project project
COPY build.sbt .
RUN sbt update
COPY . .
RUN sbt stage

FROM eclipse-temurin:${ECLIPSE_TEMURIN_VERSION}-jre-alpine
LABEL authors="fidaw"
WORKDIR /app
COPY --from=builder /build/target/universal/stage/. .
CMD ["./bin/traveltime-scala-internship-task", "--", "json_files/locations.json", "json_files/regions.json", "json_files/output.json"]