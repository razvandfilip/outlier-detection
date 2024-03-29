package razvan.filip.outlierdetectionapp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.model.Reading;

import java.util.stream.Collectors;

@Service
public class MessageListener {

    public static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    private DataPointService dataPointService;

    @Autowired
    public MessageListener(DataPointService dataPointService) {
        this.dataPointService = dataPointService;
    }

    @KafkaListener(topics = "readings", groupId = "group_id")
    public void listen(Reading message) {
        logger.info("Got message from topic: {}, {}, {}", message.getPublisherId(), message.getTime(),
                message.getReadings().stream().map(String::valueOf).collect(Collectors.joining(",")));
        dataPointService.saveDataPoint(message);
    }
}
