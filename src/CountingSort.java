public class CountingSort {
    public static void countingSort(int[] arr, int n) {
        int max = getMax(arr);//find range
        int[] count = new int[max + 1];
        int[] output = new int[n];

        // Frequency Count
        for (int i = 0; i < n; i++) {
            count[arr[i]]++;
        }

        // Prefix Sum (Cumulative Count)
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        // Build Output Array
        for (int i = n - 1; i >= 0; i--) {
            output[count[arr[i]] - 1] = arr[i];
            count[arr[i]]--;
        }

        // Copy back to original array
        System.arraycopy(output, 0, arr, 0, n);
    }

    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int x : arr) if (x > max) max = x;
        return max;
    }

    public static int getMin(int[] arr) {
        int min = arr[0];
        for (int x : arr) if (x < min) min = x;
        return min;
    }
}
