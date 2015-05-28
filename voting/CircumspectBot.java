import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class CircumspectBot implements Player {

    static class IndexComparator implements Comparator<Integer> {
        private int [] _payoffs;
        public IndexComparator(int [] payoffs) {
            _payoffs = payoffs;
        }

        public int compare(Integer i1, Integer i2) {
            return _payoffs[i2] - _payoffs[i1];
        }
    }

    public CircumspectBot(int elections) {
    }

    @Override
    public String getName() {
        return "CircumspectBot";
    }

    @Override
    public int getVote(int[] voteCounts, int votersRemaining, int[] payoffs,
            int[] totalPayoffs) {
        List<Integer> indexes = new ArrayList<Integer>();

        int topVote = voteCounts[0];
        if (voteCounts[1] > topVote)
            topVote = voteCounts[1];
        if (voteCounts[2] > topVote)
            topVote = voteCounts[2];

        for (int index = 0; index < 3; index++) {
            if (voteCounts[index] + votersRemaining + 1 >= topVote) {
                indexes.add(index);
            }
        }
        Collections.sort(indexes, new IndexComparator(payoffs));

        return indexes.get(0);
    }

    @Override
    public void receiveResults(int[] voteCounts, double result) {

    }

}
