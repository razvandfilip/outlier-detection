package razvan.filip.outlierdetectionapp.median;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class MedianCalculator {

    public <T> double median(Stream<T> stream, Function<T, Double> getValue) {
        if (stream == null) {
            throw new IllegalArgumentException("Stream should not be null.");
        }

        PriorityQueue<Double> lowerHalf = new PriorityQueue<>();
        PriorityQueue<Double> upperHalf = new PriorityQueue<>(Comparator.reverseOrder());

        stream.map(getValue).forEach(value -> {
            lowerHalf.offer(value);
            upperHalf.offer(lowerHalf.poll());
            if (lowerHalf.size() < upperHalf.size()) {
                lowerHalf.offer(upperHalf.poll());
            }
        });

        if (lowerHalf.isEmpty() && upperHalf.isEmpty()) {
            throw new IllegalArgumentException("Stream should not be empty.");
        }

        if (lowerHalf.size() == upperHalf.size()) {
            return (lowerHalf.peek() + upperHalf.peek()) / 2.0;
        } else {
            return lowerHalf.size() > upperHalf.size() ? lowerHalf.peek() : upperHalf.peek();
        }
    }
}
