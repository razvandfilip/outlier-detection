FROM openjdk:8-jdk-slim-buster AS GRADLE_BUILD

COPY outlier-detection-app/gradle /build/gradle/
COPY outlier-detection-app/src /build/src/
COPY outlier-detection-app/build.gradle outlier-detection-app/gradlew outlier-detection-app/settings.gradle /build/

WORKDIR /build/
RUN ./gradlew build

FROM openjdk:8-jre-slim-buster

RUN apt-get update || true && \
    apt-get install -y wget gnupg && \
    wget -qO - https://www.mongodb.org/static/pgp/server-4.2.asc | apt-key add - && \
    echo "deb http://repo.mongodb.org/apt/debian buster/mongodb-org/4.2 main" > /etc/apt/sources.list.d/mongodb-org-4.2.list

RUN apt-get update || true && \
    apt-get install -y supervisor vim procps less mongodb-org && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean && \
    mkdir /opt/kafka && \
    wget -q http://mirrors.hostingromania.ro/apache.org/kafka/2.3.0/kafka_2.12-2.3.0.tgz -O /tmp/kafka_2.12-2.3.0.tgz && \
    tar xzf /tmp/kafka_2.12-2.3.0.tgz -C /opt/kafka && \
    rm /tmp/kafka_2.12-2.3.0.tgz && \
    mkdir -p /app/logs/mongod && \
    mkdir -p /app/logs/outlier-detection

ADD supervisor/zookeeper.conf supervisor/kafka.conf supervisor/outlier-detection.conf supervisor/mongod.conf /etc/supervisor/conf.d/
ADD run.sh /app/
RUN chmod +x /app/run.sh

COPY --from=GRADLE_BUILD /build/build/libs/outlier-detection-app-0.0.1-SNAPSHOT.jar /app/

CMD ["supervisord", "-n"]
