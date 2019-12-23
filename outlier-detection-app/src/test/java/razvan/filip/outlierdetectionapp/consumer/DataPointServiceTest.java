package razvan.filip.outlierdetectionapp.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import razvan.filip.outlierdetectionapp.entity.DataPoint;
import razvan.filip.outlierdetectionapp.repository.DataPointRepository;
import razvan.filip.outlierdetectionapp.model.Reading;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataPointServiceTest {

    @Mock
    private ReadingValidator readingValidator;

    @Mock
    private DataPointMapper dataPointMapper;

    @Mock
    private DataPointRepository dataPointRepository;

    @InjectMocks
    private DataPointService dataPointService;

    @Test
    public void testValidSave() {
        Reading reading = new Reading();
        DataPoint dataPoint = new DataPoint();
        when(readingValidator.isValid(reading)).thenReturn(true);
        when(dataPointMapper.mapFromReading(reading)).thenReturn(dataPoint);

        dataPointService.saveDataPoint(reading);

        verify(readingValidator).isValid(reading);
        verify(dataPointMapper).mapFromReading(reading);
        verify(dataPointRepository).insert(dataPoint);
    }

    @Test
    public void testInvalidSave() {
        Reading reading = new Reading();
        when(readingValidator.isValid(reading)).thenReturn(false);

        dataPointService.saveDataPoint(reading);

        verify(readingValidator).isValid(reading);
        verifyNoInteractions(dataPointMapper, dataPointRepository);
    }
}
