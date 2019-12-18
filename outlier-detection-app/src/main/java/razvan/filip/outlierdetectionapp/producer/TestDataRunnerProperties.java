package razvan.filip.outlierdetectionapp.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("test-data")
public class TestDataRunnerProperties {
    private int testDuration;

    public int getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(int testDuration) {
        this.testDuration = testDuration;
    }
}
