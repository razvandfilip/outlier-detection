package razvan.filip.outlierdetectionapp.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.consumer.entity.DataPoint;
import razvan.filip.outlierdetectionapp.consumer.repository.DataPointRepository;
import razvan.filip.outlierdetectionapp.model.Reading;

@Service
public class DataPointService {

    private DataPointMapper dataPointMapper;
    private DataPointRepository dataPointRepository;

    @Autowired
    public DataPointService(DataPointMapper dataPointMapper, DataPointRepository dataPointRepository) {
        this.dataPointMapper = dataPointMapper;
        this.dataPointRepository = dataPointRepository;
    }

    public void saveDataPoint(Reading reading) {

        DataPoint dataPoint = dataPointMapper.mapFromReading(reading);

        dataPointRepository.insert(dataPoint);

    }
}
