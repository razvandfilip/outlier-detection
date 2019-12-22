package razvan.filip.outlierdetectionapp.consumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import razvan.filip.outlierdetectionapp.consumer.entity.DataPoint;

@Repository
public interface DataPointRepository extends MongoRepository<DataPoint, String> {

}
