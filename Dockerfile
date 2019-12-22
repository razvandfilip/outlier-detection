FROM openjdk:8-alpine AS GRADLE_BUILD

COPY outlier-detection-app/gradle /build/gradle/
COPY outlier-detection-app/src /build/src/
COPY outlier-detection-app/build.gradle outlier-detection-app/gradlew outlier-detection-app/settings.gradle /build/

WORKDIR /build/
RUN ./gradlew build

FROM java:openjdk-8-jre

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6 && \
    echo "deb http://repo.mongodb.org/apt/debian jessie/mongodb-org/3.4 main" > /etc/apt/sources.list.d/mongodb-org-3.4.list

RUN apt-get update || true && \
    apt-get install -y wget supervisor vim less mongodb-org && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean && \
    mkdir /opt/kafka && \
    wget -q http://mirrors.hostingromania.ro/apache.org/kafka/2.3.0/kafka_2.12-2.3.0.tgz -O /tmp/kafka_2.12-2.3.0.tgz && \
    tar xzf /tmp/kafka_2.12-2.3.0.tgz -C /opt/kafka && \
    rm /tmp/kafka_2.12-2.3.0.tgz && \
    mkdir -p /app/logs/mongod && \
    mkdir -p /app/logs/outlier-detection

ADD supervisor/zookeeper.conf supervisor/kafka.conf supervisor/outlier-detection.conf supervisor/mongod.conf /etc/supervisor/conf.d/

COPY --from=GRADLE_BUILD /build/build/libs/outlier-detection-app-0.0.1-SNAPSHOT.jar /app/

CMD ["supervisord", "-n"]
