package razvan.filip.outlierdetectionapp.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import razvan.filip.outlierdetectionapp.consumer.entity.DataPoint;
import razvan.filip.outlierdetectionapp.model.Reading;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataPointMapperTest {

    @Mock
    private MedianCalculator medianCalculator;

    @InjectMocks
    private DataPointMapper dataPointMapper;

    @Test
    public void testMapping() {
        String publisherId = "p1";
        LocalDateTime today = LocalDateTime.of(2019, 12, 24, 16, 48, 12, 453);
        List<Integer> readingValues = Arrays.asList(1, 2, 3);
        Reading input = new Reading(
                publisherId,
                today,
                readingValues);
        when(medianCalculator.median(readingValues)).thenReturn(2.0);
        DataPoint result = dataPointMapper.mapFromReading(input);
        verify(medianCalculator).median(readingValues);
        assertThat(result)
                .extracting(DataPoint::getPublisherId, DataPoint::getTime, DataPoint::getDataValue)
                .containsExactly(publisherId, today, 2.0);
    }
}
