package razvan.filip.outlierdetectionapp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import razvan.filip.outlierdetectionapp.model.Reading;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeneratorTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorTask.class);
    private int duration;
    private ThreadLocalRandom random;
    private MessageProducer producer;
    private String publisherId;
    
    public GeneratorTask(MessageProducer producer, int duration, String publisherId) {
        this.producer = producer;
        this.duration = duration;
        this.publisherId = publisherId;
        this.random = ThreadLocalRandom.current();
    }

    @Override
    public void run() {
        final long endTime = System.currentTimeMillis() + duration*1000;

        while (System.currentTimeMillis() < endTime) {
           Reading reading = generateReadings();
           producer.send(reading);
           try {
               Thread.sleep(random.nextInt(1800) + 200);
           } catch (InterruptedException ignored) {}
        }
    }

    private Reading generateReadings() {
        Reading reading = new Reading();
        reading.setPublisherId(publisherId);
        reading.setTime(LocalDateTime.now());
        if (random.nextInt(10) == 0) {
            int outlierMedianValue = random.nextInt(6000, 7000);
            Stream<Integer> smallerThanMedian = random.ints(5, 5000, 6000).boxed();
            Stream<Integer> largerThanMedian = random.ints(5, 7000, 8000).boxed();

            reading.setReadings(Stream.of(Stream.of(outlierMedianValue), smallerThanMedian, largerThanMedian)
                    .flatMap(i -> i).collect(Collectors.toList()));

            logger.info("Publisher id {} produced outlier value {}", publisherId, outlierMedianValue);
        } else {
            reading.setReadings(
                    random.ints(random.nextInt(10) + 5,0 ,5000)
                            .boxed()
                            .collect(Collectors.toList())
            );
        }

        return reading;
    }
}
