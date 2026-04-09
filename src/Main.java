import java.util.Random;
import java.util.Arrays;

public class Main {
    // Threshold C from paper
    private static final int C = 1000;

    public static void main(String[] args) {
        runTable1();
        runTable2();
        runTable3();
    }

    /**
     * TABLE 1: Counting Sort - Random vs. Sorted
     * Proves that data locality makes Counting Sort faster.
     */
    public static void runTable1() {
        System.out.println("--- Table 1: Counting Sort (Random vs. Sorted) ---");
        int[] ns = {1000000, 2000000, 3000000};
        CountingSort cs = new CountingSort();3

        for (int n : ns) {
            int[] randomData = generateRandomArray(n, n);
            int[] sortedData = randomData.clone();
            Arrays.sort(sortedData);

            // T1: Random
            long start = System.nanoTime();
            cs.countingSort(randomData, n);
            double t1 = (System.nanoTime() - start) / 1_000_000.0;

            // T2: Sorted
            start = System.nanoTime();
            cs.countingSort(sortedData, n);
            double t2 = (System.nanoTime() - start) / 1_000_000.0;

            System.out.printf("n=%d, r=%d | T1 (Random): %.3f ms | T2 (Sorted): %.3f ms\n", n, n, t1, t2);
        }
        System.out.println();
    }

    /**
     * TABLE 2: Counting Sort with/without Preprocessing (Worst Case r >> n)
     * Proves that Quicksort preprocessing helps when data is scattered.
     */
    public static void runTable2() {
        System.out.println("--- Table 2: Preprocessing vs. No Preprocessing (r >> n) ---");
        int[] ns = {1000, 2000, 3000};
        int r = 1000000;
        QuickSortModified qsMod = new QuickSortModified();
        CountingSort cs = new CountingSort();

        for (int n : ns) {
            int[] data = generateRandomArray(n, r);
            int[] dataClone = data.clone();

            // T1: Hybrid (Timing steps separately as in paper)
            long startPre = System.nanoTime();
            int max = CountingSort.getMax(data);
            int min = CountingSort.getMin(data);
            qsMod.quicksort_modified(data, 0, n - 1, max, min, C);
            double preTime = (System.nanoTime() - startPre) / 1_000_000.0;

            long startCount = System.nanoTime();
            cs.countingSort(data, n);
            double countTime = (System.nanoTime() - startCount) / 1_000_000.0;

            // T2: Classic Counting Sort
            long startT2 = System.nanoTime();
            cs.countingSort(dataClone, n);
            double t2 = (System.nanoTime() - startT2) / 1_000_000.0;

            System.out.printf("n=%d, r=%d | T1 (Hybrid): %.2f+%.2f ms | T2 (Classic): %.2f ms\n", n, r, preTime, countTime, t2);
        }
        System.out.println();
    }

    /**
     * TABLE 3: The Big Comparison (n = r)
     * Standard QuickSort, Quicksort+Insertion, and Hybrid Sort.
     */
    public static void runTable3() {
        System.out.println("--- Table 3: Quicksort, Quicksort+Insertion, and Hybrid (n = r) ---");
        int[] ns = {1000000, 2000000};

        for (int n : ns) {
            int[] data = generateRandomArray(n, n);

            // T1: Standard Quicksort
            int[] qsData = data.clone();
            long startT1 = System.nanoTime();
            new QuickSort().sort(qsData, 0, n - 1);
            double t1 = (System.nanoTime() - startT1) / 1_000_000.0;

            // T2: Quicksort + Insertion Sort
            int[] dataT2 = data.clone();
            long startT2 = System.nanoTime();
            new QuickSortInsertion().sort(dataT2, 0, n - 1);
            double t2 = (System.nanoTime() - startT2) / 1_000_000.0;

            // T3: Proposed Hybrid Sort
            int[] hybridData = data.clone();
            long startT3 = System.nanoTime();
            hybridSort(hybridData, n, C);
            double t3 = (System.nanoTime() - startT3) / 1_000_000.0;

            System.out.printf("n=r=%d | T1 (Quicksort): %.2f ms | T2 (QS+Ins): %.2f ms | T3 (Hybrid): %.2f ms\n", n, t1, t2, t3);
        }
    }

    public static void hybridSort(int[] arr, int n, int C) {
        QuickSortModified qsMod = new QuickSortModified();
        CountingSort cs = new CountingSort();
        int max = CountingSort.getMax(arr);
        int min = CountingSort.getMin(arr);
        qsMod.quicksort_modified(arr, 0, n - 1, max, min, C);
        cs.countingSort(arr, n);
    }

    private static int[] generateRandomArray(int n, int range) {
        Random rd = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rd.nextInt(range);
        return arr;
    }
}
