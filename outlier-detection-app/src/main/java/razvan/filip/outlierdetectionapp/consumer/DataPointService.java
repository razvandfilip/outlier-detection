package razvan.filip.outlierdetectionapp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.entity.DataPoint;
import razvan.filip.outlierdetectionapp.repository.DataPointRepository;
import razvan.filip.outlierdetectionapp.model.Reading;

@Service
public class DataPointService {

    private static final Logger logger = LoggerFactory.getLogger(DataPointService.class);

    private ReadingValidator readingValidator;
    private DataPointMapper dataPointMapper;
    private DataPointRepository dataPointRepository;

    @Autowired
    public DataPointService(ReadingValidator readingValidator, DataPointMapper dataPointMapper, DataPointRepository dataPointRepository) {
        this.readingValidator = readingValidator;
        this.dataPointMapper = dataPointMapper;
        this.dataPointRepository = dataPointRepository;
    }

    public void saveDataPoint(Reading reading) {
        if (readingValidator.isValid(reading)) {
            DataPoint dataPoint = dataPointMapper.mapFromReading(reading);
            dataPointRepository.insert(dataPoint);
        } else {
            logger.info("Reading from topic is not valid; will not store.");
        }
    }
}
