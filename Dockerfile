FROM java:openjdk-8-jre

RUN apt-get update || true && \
    apt-get install -y wget supervisor && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean && \
    mkdir /opt/kafka && \
    wget -q http://mirrors.hostingromania.ro/apache.org/kafka/2.3.0/kafka_2.12-2.3.0.tgz -O /tmp/kafka_2.12-2.3.0.tgz && \
    tar xzf /tmp/kafka_2.12-2.3.0.tgz -C /opt/kafka && \
    rm /tmp/kafka_2.12-2.3.0.tgz 

ADD supervisor/zookeeper.conf /etc/supervisor/conf.d/    

CMD ["supervisord", "-n"]
