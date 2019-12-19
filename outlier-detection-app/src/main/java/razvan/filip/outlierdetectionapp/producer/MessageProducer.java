package razvan.filip.outlierdetectionapp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private static final String TOPIC = "readings";

    private KafkaTemplate<String, Reading> kafkaTemplate;

    @Autowired
    public MessageProducer(KafkaTemplate<String, Reading> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Reading reading) {
        ListenableFuture<SendResult<String, Reading>> send = kafkaTemplate.send(TOPIC, reading);
        send.addCallback(new ListenableFutureCallback<SendResult<String, Reading>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.error("Could not sent message to kafka topic", ex);
            }

            @Override
            public void onSuccess(SendResult<String, Reading> result) {
                logger.info("Sent message to kafka topic");
            }
        });
    }
}
