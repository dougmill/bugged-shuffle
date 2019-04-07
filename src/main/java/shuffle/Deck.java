package shuffle;

import static shuffle.Deck.RelevantEnd.FRONT;

import org.apache.commons.math3.random.MersenneTwister;

import java.util.Arrays;

public class Deck {
    private final int[] decklist;
    private final int[] deck;
    private final int mulligans;
    private final int relevantCards;
    private final MersenneTwister random = new MersenneTwister();

    public Deck(int size, int mulligans, int relevantCards, RelevantEnd end) {
        decklist = new int[size];
        for (int i = 0; i < size; i++) {
            decklist[i] = end == FRONT ? i : size - 1 - i;
        }
        deck = Arrays.copyOf(decklist, size);
        this.mulligans = mulligans;
        this.relevantCards = relevantCards;
    }

    public int generateRelevantCardsInHand() {
        System.arraycopy(decklist, 0, deck, 0, deck.length);
        for (int m = 0; m <= mulligans; m++) {
            for (int i = 0; i < deck.length; i++) {
                int swapIndex = random.nextInt(deck.length); // intentionally incorrect
                int temp = deck[i];
                deck[i] = deck[swapIndex];
                deck[swapIndex] = temp;
            }
        }
        int handSize = 7 - mulligans;
        int count = 0;
        for (int i = 0; i < handSize; i++) {
            if (deck[i] < relevantCards) {
                count++;
            }
        }
        return count;
    }

    public enum RelevantEnd {
        FRONT, BACK
    }
}
