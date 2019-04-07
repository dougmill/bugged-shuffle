package shuffle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Table {
    private final int mulligans;
    private final Row[] rows;

    public Table(int size, int[] relevantCardAmounts, int mulligans) {
        this.mulligans = mulligans;
        rows = new Row[relevantCardAmounts.length * 2];
        for (int i = 0; i < relevantCardAmounts.length; i++) {
            rows[2 * i] = new Row(size, mulligans, relevantCardAmounts[i], Deck.RelevantEnd.FRONT);
            rows[2 * i + 1] = new Row(size, mulligans, relevantCardAmounts[i], Deck.RelevantEnd.BACK);
        }
    }

    public List<Future<?>> generateMoreStats(long games, ExecutorService executor) {
        return Arrays.stream(rows)
                .map(row -> executor.submit(() -> row.generateMoreStats(games)))
                .collect(Collectors.toList());
    }

    public String toProportionString() {
        return toStringHelper(Row::toProportionString, Justified.LEFT);
    }

    @Override
    public String toString() {
        return toStringHelper(Row::toString, Justified.RIGHT);
    }

    private String toStringHelper(Function<Row, String> converter, Justified justified) {
        String headers = "||" + IntStream.range(0, 8 - mulligans)
                .mapToObj(i -> i + " in hand")
                .collect(Collectors.joining("|")) + "|";
        String separatorRow = "|" + String.join("", Collections.nCopies(9 - mulligans, justified.getMarkdown()));
        return Stream.concat(Stream.of(headers, separatorRow), Arrays.stream(rows).map(converter))
                .collect(Collectors.joining("\n"));
    }

    private enum Justified {
        LEFT(":-|"),
        RIGHT("-:|");

        private final String markdown;

        Justified(String markdown) {
            this.markdown = markdown;
        }

        public String getMarkdown() {
            return markdown;
        }
    }
}
