package shuffle;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class Row {
    private final Deck deck;
    private final long[] results;
    private final int relevantCards;
    private final Deck.RelevantEnd end;

    public Row(int size, int mulligans, int relevantCards, Deck.RelevantEnd end) {
        this.deck = new Deck(size, mulligans, relevantCards, end);
        results = new long[8 - mulligans];
        this.relevantCards = relevantCards;
        this.end = end;
    }

    public void generateMoreStats(long games) {
        for (long i = 0; i < games; i++) {
            results[deck.generateRelevantCardsInHand()]++;
        }
    }

    public String toProportionString() {
        long total = Arrays.stream(results).sum();
        return toStringHelper(result ->
                new BigDecimal(((double) result) / total).setScale(6, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Override
    public String toString() {
        return toStringHelper(String::valueOf);
    }

    private String toStringHelper(LongFunction<String> converter) {
        String prefix = "|" + relevantCards + " " + end.name().toLowerCase() + "|";
        return Arrays.stream(results).mapToObj(converter).collect(Collectors.joining("|", prefix, "|"));
    }
}
