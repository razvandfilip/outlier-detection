package razvan.filip.outlierdetectionapp.outliers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import razvan.filip.outlierdetectionapp.consumer.entity.DataPoint;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class OutlierService {

    private static final Logger logger = LoggerFactory.getLogger(OutlierService.class);

    private MongoTemplate mongoTemplate;

    @Autowired
    public OutlierService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public String getOutliers(String publisherId, int windowSize) {


        Criteria findCriteria = Criteria.where("publisherId").is(publisherId).and("time").lte(LocalDateTime.now());
        Query query = new Query(findCriteria);
        query.with(Sort.by(Sort.Direction.DESC, "time"));
        query.limit(windowSize);
        List<DataPoint> dataPoints = mongoTemplate.find(query, DataPoint.class);

        logger.info("Found {} dataPoints for publisherId {}", dataPoints.size(), publisherId);

        DoubleStream values = dataPoints.stream().mapToDouble(DataPoint::getDataValue);
        double median = median(values, dataPoints.size());

        logger.info("Their median is {}", median);

        // calculate median
        // median (datapoints)

//        dataPoints.sort((Comparator.comparing(DataPoint::getTime)));

        /*double median = 0;

        if (dataPoints.size() % 2 == 0) {
            median = (dataPoints.get(dataPoints.size() / 2).getDataValue() + dataPoints.get(dataPoints.size() / 2 - 1).getDataValue()) / 2;
        } else {
            median = dataPoints.get(dataPoints.size() / 2).getDataValue();
        }*/

        // calculate absolute deviation from median
        // for datapoints, map datapoint -> abs (datapoint - median)

        DoubleStream deviationsFromMedian = dataPoints.stream().mapToDouble(DataPoint::getDataValue).map(value -> Math.abs(value - median));
        Double medianAbsoluteDeviation = median(deviationsFromMedian, dataPoints.size());

        logger.info("Their median absolute deviation is {}", medianAbsoluteDeviation);

        // calculate median of absolute deviations from the median
        // median (deviations)

        List<DataPoint> outliers2 = dataPoints.stream().filter(dataPoint -> Math.abs(dataPoint.getDataValue() - median) > 3 * medianAbsoluteDeviation)
                .collect(Collectors.toList());

        logger.info("There are {} outliers", outliers2.size());

        String outliers = dataPoints.stream().filter(dataPoint -> Math.abs(dataPoint.getDataValue() - median) > 3 * medianAbsoluteDeviation)
                .map(dataPoint ->
                        "[id " + dataPoint.getId() +
                                " pid " + dataPoint.getPublisherId() +
                                " time " + dataPoint.getTime() +
                                " value " + dataPoint.getDataValue() + "]")
                .collect(Collectors.joining(","));

        logger.info("Result: {}", outliers);

        // for datapoints, filter datapoint -> abs(datapoint - median) > 5 * MAD

        return outliers;
    }

    private Double median(DoubleStream values, int size) {
        DoubleStream sortedValues = values.sorted();
        return size % 2 == 0 ?
                sortedValues.skip(size/2-1).limit(2).average().getAsDouble():
                sortedValues.skip(size/2).findFirst().getAsDouble();
    }
}
