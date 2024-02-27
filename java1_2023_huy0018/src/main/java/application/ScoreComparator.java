package application;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Score> {

    @Override
    public int compare(Score o1, Score o2) {
        return Integer.compare(o2.getScore(), o1.getScore());
    }

}