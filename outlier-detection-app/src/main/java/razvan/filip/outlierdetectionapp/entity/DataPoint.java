package razvan.filip.outlierdetectionapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDateTime;

@CompoundIndex(def = "{'publisherId':1, 'time':-1}", name = "compound_index")
public class DataPoint {

    @Id
    private String id;
    private String publisherId;
    private LocalDateTime time;
    private double dataValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getDataValue() {
        return dataValue;
    }

    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }
}
