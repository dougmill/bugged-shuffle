package shuffle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Shuffler
{
    public static void main( String[] args )
    {
        Table[] tables = new Table[4];
        tables[0] = new Table(60, new int[]{ 22, 23, 24, 25 }, 0);
        tables[1] = new Table(60, new int[]{ 22, 23, 24, 25 }, 1);
        tables[2] = new Table(40, new int[]{ 15, 16, 17, 18 }, 0);
        tables[3] = new Table(40, new int[]{ 15, 16, 17, 18 }, 1);

        ExecutorService executor = Executors.newWorkStealingPool();

        long increment = 10000000L;
        for (long numGames = increment; numGames <= 1000000000L; numGames += increment) {
            List<Future<?>> futures = new ArrayList<>();
            for (Table table : tables) {
                futures.addAll(table.generateMoreStats(increment, executor));
            }
            futures.forEach(f -> {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            for (Table table : tables) {
                System.out.println(table.toString() + "\n");
            }
            for (Table table : tables) {
                System.out.println(table.toProportionString() + "\n");
            }
            System.out.println(numGames + " Games\n");
        }
    }
}
