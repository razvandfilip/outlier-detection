FROM openjdk:8-alpine AS GRADLE_BUILD

COPY outlier-detection-app/gradle /build/gradle/
COPY outlier-detection-app/src /build/src/
COPY outlier-detection-app/build.gradle outlier-detection-app/gradlew outlier-detection-app/settings.gradle /build/

WORKDIR /build/
RUN ./gradlew build

FROM java:openjdk-8-jre

RUN apt-get update || true && \
    apt-get install -y wget supervisor vim less && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean && \
    mkdir /opt/kafka && \
    wget -q http://mirrors.hostingromania.ro/apache.org/kafka/2.3.0/kafka_2.12-2.3.0.tgz -O /tmp/kafka_2.12-2.3.0.tgz && \
    tar xzf /tmp/kafka_2.12-2.3.0.tgz -C /opt/kafka && \
    rm /tmp/kafka_2.12-2.3.0.tgz 

ADD supervisor/zookeeper.conf supervisor/kafka.conf supervisor/outlier-detection.conf /etc/supervisor/conf.d/

COPY --from=GRADLE_BUILD /build/build/libs/outlier-detection-app-0.0.1-SNAPSHOT.jar /app/

CMD ["supervisord", "-n"]
