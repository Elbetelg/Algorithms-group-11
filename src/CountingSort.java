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

    // hybrid
    public static void countingSortRange(int[] arr, int low, int high, int min, int max) {

        int range = max - min + 1;

        int[] count = new int[range];
        int[] output = new int[high - low + 1];

        // 1. Frequency count
        for (int i = low; i <= high; i++) {
            count[arr[i] - min]++;
        }

        // 2. Prefix sum
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        // 3. Build output (IMPORTANT: iterate backwards)
        for (int i = high; i >= low; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }

        // 4. Copy back into original array
        for (int i = 0; i < output.length; i++) {
            arr[low + i] = output[i];
        }
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
