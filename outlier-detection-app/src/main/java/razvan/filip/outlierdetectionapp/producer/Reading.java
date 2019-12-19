package razvan.filip.outlierdetectionapp.producer;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class Reading {
    private String publisherId;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss.SSS")
    private LocalDateTime time;
    private List<Integer> readings;

    public Reading() {
    }

    public Reading(String publisherId, LocalDateTime time, List<Integer> readings) {
        this.publisherId = publisherId;
        this.time = time;
        this.readings = readings;
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

    public List<Integer> getReadings() {
        return readings;
    }

    public void setReadings(List<Integer> readings) {
        this.readings = readings;
    }
}
