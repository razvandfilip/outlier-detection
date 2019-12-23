package razvan.filip.outlierdetectionapp.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.entity.DataPoint;
import razvan.filip.outlierdetectionapp.median.MedianCalculator;
import razvan.filip.outlierdetectionapp.model.Reading;

@Service
public class DataPointMapper {

    private MedianCalculator medianCalculator;

    @Autowired
    public DataPointMapper(MedianCalculator medianCalculator) {
        this.medianCalculator = medianCalculator;
    }

    public DataPoint mapFromReading(Reading reading) {
        DataPoint dataPoint = new DataPoint();
        dataPoint.setPublisherId(reading.getPublisherId());
        dataPoint.setTime(reading.getTime());
        dataPoint.setDataValue(medianCalculator.median(reading.getReadings().stream(), Double::valueOf));
        return dataPoint;
    }
}
