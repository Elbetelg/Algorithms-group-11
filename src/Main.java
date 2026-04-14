import java.util.Random;
import java.util.Arrays;

public class Main {
    // Threshold C from paper
    private static final int C = 1000;
    private static final int NUM_TRIALS = 20;
    private static final Random rd = new Random(42);


    public static void main(String[] args) {
        System.out.println("Warming up JVM...");
        for (int i = 0; i < 10; i++) {
            int[] warmData = generateRandomArray(100000, 100000);
            hybridSort(warmData, 100000, C);
        }
        System.out.println("Warm-up complete.\n");

        runTable1();
        runTable2();
        runTable3();
    }

    /**
     * TABLE 1: Counting Sort - Random vs. Sorted
     * Proves that data locality makes Counting Sort faster.
     */
    public static void runTable1() {
        System.out.println("--- Table 1: Counting Sort (Random vs. Sorted) - Average of " + NUM_TRIALS + " trials ---");
        int[] ns = {1000000, 2000000, 3000000};
        CountingSort cs = new CountingSort();

        for (int n : ns) {
            double totalT1 = 0;
            double totalT2 = 0;
            for (int i = 0; i < NUM_TRIALS; i++) {
                int[] randomData = generateRandomArray(n, n);
                int[] sortedData = generateSortedArray(n);

                // T1: Random
                long start = System.nanoTime();
                cs.countingSort(randomData, n);
                totalT1 += (System.nanoTime() - start) / 1_000_000.0;

                // T2: Sorted
                start = System.nanoTime();
                cs.countingSort(sortedData, n);
                totalT2 += (System.nanoTime() - start) / 1_000_000.0;
            }
            System.out.printf("n=%d, r=%d | T1 (Random Avg): %.3f ms | T2 (Sorted Avg): %.3f ms\n",
                    n, n, totalT1 / NUM_TRIALS, totalT2 / NUM_TRIALS);
        }
        System.out.println();
    }

    /**
     * TABLE 2: Counting Sort with/without Preprocessing (Worst Case r >> n)
     * Proves that Quicksort preprocessing helps when data is scattered.
     */

    public static void runTable2() {
        System.out.println("--- Table 2: Preprocessing vs. No Preprocessing - Average of " + NUM_TRIALS + " trials ---");

        int[] ns = {1000, 2000, 3000};
        int r = 1_000_000;
        int INNER_REPS = 200;

        CountingSort cs = new CountingSort();

        for (int n : ns) {
            double totalPre = 0;
            double totalCount = 0;
            double totalClassic = 0;

            for (int i = 0; i < NUM_TRIALS; i++) {
                int[][] baseCases = new int[INNER_REPS][];
                for (int k = 0; k < INNER_REPS; k++) {
                    baseCases[k] = generateRandomArray(n, r);
                }

                // T1: Hybrid (measure preprocessing and counting separately)
                for (int k = 0; k < INNER_REPS; k++) {
                    int[] data = baseCases[k].clone();
                    QuickSortModified qsMod = new QuickSortModified();

                    int max = CountingSort.getMax(data);
                    int min = CountingSort.getMin(data);

                    long startPre = System.nanoTime();
                    qsMod.quicksort_modified(data, 0, n - 1, max, min, C);
                    totalPre += (System.nanoTime() - startPre) / 1_000_000.0;

                    long startCount = System.nanoTime();
                    for (Partition p : qsMod.partitions) {
                        CountingSort.countingSortRange(data, p.low, p.high, p.min, p.max);
                    }
                    totalCount += (System.nanoTime() - startCount) / 1_000_000.0;
                }

                // T2: Classic Counting Sort on the same inputs
                long startClassic = System.nanoTime();
                for (int k = 0; k < INNER_REPS; k++) {
                    int[] dataClone = baseCases[k].clone();
                    cs.countingSort(dataClone, n);
                }
                totalClassic += (System.nanoTime() - startClassic) / 1_000_000.0;
            }

            double denom = NUM_TRIALS * INNER_REPS;

            System.out.printf(
                    "n=%d, r=%d | T1 (Hybrid Avg): %.2f+%.2f ms | T2 (Classic Avg): %.2f ms%n",
                    n, r,
                    totalPre / denom,
                    totalCount / denom,
                    totalClassic / denom
            );
        }
        System.out.println();
    }

    /**
     * TABLE 3: The Big Comparison (n = r)
     * Standard QuickSort, Quicksort+Insertion, and Hybrid Sort.
     */
    public static void runTable3() {
        System.out.println("--- Table 3: Quicksort, Quicksort+Insertion, and Hybrid (n = r) - Average of " + NUM_TRIALS + " trials ---");
        int[] ns = {1000000, 2000000};

        for (int n : ns) {
            double totalT1 = 0;
            double totalT2 = 0;
            double totalT3 = 0;

            for (int i = 0; i < NUM_TRIALS; i++) {
                int[] data = generateRandomArray(n, n);

                // T1: Standard Quicksort
                int[] qsData = data.clone();
                long startT1 = System.nanoTime();
                new QuickSort().sort(qsData, 0, n - 1);
                totalT1 += (System.nanoTime() - startT1) / 1_000_000.0;

                // T2: Quicksort + Insertion Sort
                int[] dataT2 = data.clone();
                long startT2 = System.nanoTime();
                new QuickSortInsertion().sort(dataT2, 0, n - 1);
                totalT2 += (System.nanoTime() - startT2) / 1_000_000.0;

                // T3: Proposed Hybrid Sort
                int[] hybridData = data.clone();
                long startT3 = System.nanoTime();
                hybridSort(hybridData, n, C);
                totalT3 += (System.nanoTime() - startT3) / 1_000_000.0;
            }
            System.out.printf("n=r=%d | T1 (QS): %.2f ms | T2 (QS+Ins): %.2f ms | T3 (Hybrid): %.2f ms\n",
                    n, totalT1 / NUM_TRIALS, totalT2 / NUM_TRIALS, totalT3 / NUM_TRIALS);
        }
    }

    public static void hybridSort(int[] arr, int n, int C) {
        QuickSortModified qsMod = new QuickSortModified();
        CountingSort cs = new CountingSort();
        int max = CountingSort.getMax(arr);
        int min = CountingSort.getMin(arr);

        qsMod.partitions.clear();
        qsMod.quicksort_modified(arr, 0, n - 1, max, min, C);

        // counting sort  applied per partition
        for (Partition p : qsMod.partitions) {
            CountingSort.countingSortRange(arr, p.low, p.high, p.min, p.max);
        }
    }

    private static int[] generateRandomArray(int n,int range) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rd.nextInt(range);
        return arr;
    }

    private static int[] generateSortedArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i * (1000000 / n); // Linearly increasing
        return arr;
    }
}
