package razvan.filip.outlierdetectionapp.producer;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestDataRunner.class);

    private TestDataRunnerProperties properties;
    private MessageProducer producer;
    private Tbean tbean;

    @Autowired
    public TestDataRunner(TestDataRunnerProperties properties, MessageProducer producer, Tbean tbean) {
        this.properties = properties;
        this.producer = producer;
        this.tbean = tbean;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("From clr"); 

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Runnable taskSubmitter = () -> {
            logger.info("Scheduling more tasks");
            executorService.execute(new GeneratorTask(producer, tbean, 3));
            executorService.execute(new GeneratorTask(producer, tbean, 10));
            executorService.execute(new GeneratorTask(producer, tbean, 20));
        };

        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(taskSubmitter, 0, 5, TimeUnit.SECONDS);

        Runnable cancelTest = () -> {
            scheduledFuture.cancel(true);
            scheduledExecutorService.shutdown();
            executorService.shutdown();
        };
        scheduledExecutorService.schedule(cancelTest, properties.getTestDuration(), TimeUnit.SECONDS);
    }
}
