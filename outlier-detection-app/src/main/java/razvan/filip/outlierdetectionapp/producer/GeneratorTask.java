package razvan.filip.outlierdetectionapp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GeneratorTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorTask.class);
    private Tbean tbean;
    private int duration;
    private Random random;
    
    public GeneratorTask(Tbean tbean, int duration) {
        this.tbean = tbean;
        this.duration = duration;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        final long endTime = System.currentTimeMillis() + duration*1000;

        while (System.currentTimeMillis() < endTime) {
           logger.info("Logging tbean {} from task {}", tbean.get(), this.toString()); 
           try {
               Thread.sleep(1976);
           } catch (InterruptedException ignored) {}
        }
    }
}
