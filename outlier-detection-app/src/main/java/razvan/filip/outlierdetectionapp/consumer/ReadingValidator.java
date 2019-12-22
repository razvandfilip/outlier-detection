package razvan.filip.outlierdetectionapp.consumer;

import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.model.Reading;

@Service
public class ReadingValidator {

    public boolean isValid(Reading reading) {
        return reading.getPublisherId() != null &&
                reading.getTime() != null &&
                reading.getReadings() != null &&
                !reading.getReadings().isEmpty();
    }
}
