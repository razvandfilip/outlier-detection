package razvan.filip.outlierdetectionapp.outliers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import razvan.filip.outlierdetectionapp.entity.DataPoint;
import razvan.filip.outlierdetectionapp.median.MedianCalculator;
import razvan.filip.outlierdetectionapp.repository.DataPointRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutlierServiceTest {

    @Mock
    private MedianCalculator medianCalculator;

    @Mock
    private DataPointRepository dataPointRepository;

    @InjectMocks
    private OutlierService outlierService;

    @Test
    public void testNoDataPoints() {
        String publisherId = "p1";
        int windowSize = 10;
        when(dataPointRepository.getDataPointWindow(publisherId, windowSize)).thenReturn(Collections.emptyList());

        List<DataPoint> outliers = outlierService.getOutliers(publisherId, windowSize);
        assertThat(outliers).isEmpty();

        verify(dataPointRepository).getDataPointWindow(publisherId, windowSize);
    }

    @Test
    public void testFindOutliers() {
        String publisherId = "p1";
        int windowSize = 10;

        List<DataPoint> dataPoints = generateDataPoints(publisherId, 3, 2, 1, 3, 10, 2);

        when(dataPointRepository.getDataPointWindow(publisherId, windowSize)).thenReturn(dataPoints);

        when(medianCalculator.median(any(), any())).thenAnswer(new Answer<Object>() {
            boolean firstCall = true;
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (firstCall) {
                    firstCall = false;
                    return 2.5;
                } else {
                    return 0.5;
                }
            }
        });

        List<DataPoint> outliers = outlierService.getOutliers(publisherId, windowSize);

        verify(dataPointRepository).getDataPointWindow(publisherId, windowSize);
        verify(medianCalculator, times(2)).median(any(), any());

        assertThat(outliers)
                .hasSize(1)
                .allMatch(dataPoint -> dataPoint.getDataValue() == 10.0);
    }

    private List<DataPoint> generateDataPoints(String publisherId, double... values) {
        return Arrays.stream(values)
                .mapToObj(value -> {
                    DataPoint dataPoint = new DataPoint();
                    dataPoint.setPublisherId(publisherId);
                    dataPoint.setDataValue(value);
                    return dataPoint;
                })
                .collect(Collectors.toList());
    }
}
