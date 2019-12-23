package razvan.filip.outlierdetectionapp.outliers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.entity.DataPoint;
import razvan.filip.outlierdetectionapp.median.MedianCalculator;
import razvan.filip.outlierdetectionapp.repository.DataPointRepository;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OutlierService {

    private static final Logger logger = LoggerFactory.getLogger(OutlierService.class);

    private MedianCalculator medianCalculator;
    private DataPointRepository dataPointRepository;

    @Autowired
    public OutlierService(MedianCalculator medianCalculator, DataPointRepository dataPointRepository) {
        this.medianCalculator = medianCalculator;
        this.dataPointRepository = dataPointRepository;
    }


    public List<DataPoint> getOutliers(String publisherId, int windowSize) {

        List<DataPoint> dataPoints = dataPointRepository.getDataPointWindow(publisherId, windowSize);

        if (dataPoints.isEmpty()) {
            logger.info("No dataPoints found for publisherId {}", publisherId);
            return Collections.emptyList();
        }

        logger.info("Found {} dataPoints for publisherId {}", dataPoints.size(), publisherId);

        double median = medianCalculator.median(dataPoints.stream(), DataPoint::getDataValue);

        logger.info("Their median is {}", median);

        double medianAbsoluteDeviation = medianCalculator.median(
                dataPoints.stream().map(dataPoint -> Math.abs(dataPoint.getDataValue() - median)),
                Function.identity());

        logger.info("Their median absolute deviation is {}", medianAbsoluteDeviation);

        List<DataPoint> foundOutliers = dataPoints.stream()
                .filter(dataPoint -> Math.abs(dataPoint.getDataValue() - median) > 5 * medianAbsoluteDeviation)
                .collect(Collectors.toList());

        logger.info("There are {} outliers: {}", foundOutliers.size(), foundOutliers);

        return foundOutliers;
    }
}
