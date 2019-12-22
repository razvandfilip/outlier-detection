package razvan.filip.outlierdetectionapp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import razvan.filip.outlierdetectionapp.model.Reading;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GeneratorTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorTask.class);
    private Tbean tbean;
    private int duration;
    private ThreadLocalRandom random;
    private MessageProducer producer;
    
    public GeneratorTask(MessageProducer producer, Tbean tbean, int duration) {
        this.producer = producer;
        this.tbean = tbean;
        this.duration = duration;
        this.random = ThreadLocalRandom.current();
    }

    @Override
    public void run() {
        final long endTime = System.currentTimeMillis() + duration*1000;

        while (System.currentTimeMillis() < endTime) {
           logger.info("Logging tbean {} from task {}", tbean.get(), this.toString());
           Reading reading = generateReadings();
           producer.send(reading);
           try {
               Thread.sleep(random.nextInt(1800) + 200);
           } catch (InterruptedException ignored) {}
        }
    }

    private Reading generateReadings() {
        Reading reading = new Reading();
        reading.setPublisherId(String.valueOf(random.nextInt(10) + 1));
        reading.setTime(LocalDateTime.now());
        reading.setReadings(
                random.ints(random.nextInt(10) + 5,0 ,5000)
                        .boxed()
                        .collect(Collectors.toList())
        );
        return reading;
    }
}
