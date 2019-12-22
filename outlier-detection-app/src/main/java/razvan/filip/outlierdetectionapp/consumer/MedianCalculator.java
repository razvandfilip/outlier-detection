package razvan.filip.outlierdetectionapp.consumer;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class MedianCalculator {

    public double median(List<Integer> list) {

        if (list == null) {
            throw new IllegalArgumentException("List should not be null.");
        }

        if (list.isEmpty()) {
            throw new IllegalArgumentException("List should not be empty.");
        }

        PriorityQueue<Integer> lowerHalf = new PriorityQueue<>();
        PriorityQueue<Integer> upperHalf = new PriorityQueue<>(Comparator.reverseOrder());

        for (Integer i : list) {
            lowerHalf.offer(i);
            upperHalf.offer(lowerHalf.poll());
            if (lowerHalf.size() < upperHalf.size()) {
                lowerHalf.offer(upperHalf.poll());
            }
        }

        if (lowerHalf.size() == upperHalf.size()) {
            return (lowerHalf.peek() + upperHalf.peek()) / 2.0;
        } else {
            return lowerHalf.size() > upperHalf.size() ? lowerHalf.peek() : upperHalf.peek();
        }
    }
}
