package razvan.filip.outlierdetectionapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import razvan.filip.outlierdetectionapp.entity.DataPoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataPointRepository {

    private MongoTemplate mongoTemplate;

    @Autowired
    public DataPointRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public DataPoint insert(DataPoint dataPoint) {
        return mongoTemplate.insert(dataPoint);
    }

    public List<DataPoint> getDataPointWindow(String publisherId, int windowSize) {
        Criteria findCriteria = Criteria.where("publisherId").is(publisherId).and("time").lte(LocalDateTime.now());
        Query query = new Query(findCriteria);
        query.with(Sort.by(Sort.Direction.DESC, "time"));
        query.limit(windowSize);
        return mongoTemplate.find(query, DataPoint.class);
    }
}
