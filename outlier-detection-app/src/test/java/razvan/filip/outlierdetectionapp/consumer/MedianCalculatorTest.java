package razvan.filip.outlierdetectionapp.consumer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MedianCalculatorTest {

    @Test
    public void testEmptyList() {
        List<Integer> integers = Collections.emptyList();
        MedianCalculator medianCalculator = new MedianCalculator();
        assertThatThrownBy(() -> medianCalculator.median(integers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("List should not be empty.");
    }

    @Test
    public void testNullParameter() {
        MedianCalculator medianCalculator = new MedianCalculator();
        assertThatThrownBy(() -> medianCalculator.median(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("List should not be null.");
    }

    @Test
    public void testSingleElementList() {
        List<Integer> integers = Collections.singletonList(5);
        MedianCalculator medianCalculator = new MedianCalculator();
        double median = medianCalculator.median(integers);
        assertThat(median).isEqualTo(5);
    }

    @Test
    public void testOddSizeList() {
        List<Integer> integers = Arrays.asList(18, 3, 42, 84, 15);
        MedianCalculator medianCalculator = new MedianCalculator();
        double median = medianCalculator.median(integers);
        assertThat(median).isEqualTo(18);
    }

    @Test
    public void testEvenSizeList() {
        List<Integer> integers = Arrays.asList(18, 3, 42, 84, 15, 1);
        MedianCalculator medianCalculator = new MedianCalculator();
        double median = medianCalculator.median(integers);
        assertThat(median).isEqualTo(16.5);
    }
}
