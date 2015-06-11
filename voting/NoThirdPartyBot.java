import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class NoThirdPartyBot implements Player {

    static class IndexComparator implements Comparator<Integer> {
        private int [] _payoffs;
        public IndexComparator(int [] payoffs) {
            _payoffs = payoffs;
        }

        public int compare(Integer i1, Integer i2) {
            return _payoffs[i2] - _payoffs[i1];
        }
    }

    public NoThirdPartyBot(int e) {
    }


    @Override
    public String getName() {
        return "NoThirdPartyBot";
    }

    @Override
    public int getVote(int[] voteCounts, int votersRemaining, int[] payoffs,
            int[] totalPayoffs) {
        List<Integer> order = order(totalPayoffs);

        if (payoffs[order.get(0)] > payoffs[order.get(1)]) {
            return order.get(0);
        } else {
            return order.get(1);
        }
    }

    static List<Integer> order(int[] array) {
        List<Integer> indexes = Arrays.asList(0, 1, 2);
        Collections.sort(indexes, new IndexComparator(array));
        return indexes;
    }

    @Override
    public void receiveResults(int[] voteCounts, double result) {
    }
}
