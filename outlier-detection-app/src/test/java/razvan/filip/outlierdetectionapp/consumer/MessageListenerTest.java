package razvan.filip.outlierdetectionapp.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import razvan.filip.outlierdetectionapp.model.Reading;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageListenerTest {

    @Mock
    private DataPointService dataPointService;

    @InjectMocks
    private MessageListener messageListener;

    @Test
    public void testListen() {
        Reading reading = new Reading(
                "p1",
                LocalDateTime.of(2019, 12, 22, 14, 32, 57, 435),
                Arrays.asList(1, 2, 3));
        messageListener.listen(reading);
        verify(dataPointService).saveDataPoint(reading);
    }
}
