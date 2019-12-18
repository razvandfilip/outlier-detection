package razvan.filip.outlierdetectionapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class OutlierDetectionAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutlierDetectionAppApplication.class, args);
    }

}
