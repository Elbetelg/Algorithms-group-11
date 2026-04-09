public class QuickSortModified {
    public void quicksort_modified(int[] arr, int low, int high, int maxValue, int minValue, int C) {
        // The threshold check: Stop recursion if (Range + Size) <= C
        if ((high - low) > 0 && (maxValue - minValue + (high - low)) > C) {

            // 1. Median-of-Three Pivot Selection
            int pivotIndex = partition(arr, low, high);
            int midValue = arr[pivotIndex];

            // 2. Recursive calls for sub-partitions
            quicksort_modified(arr, low, pivotIndex - 1, midValue, minValue, C);
            quicksort_modified(arr, pivotIndex + 1, high, maxValue, midValue, C);
        }
        // If the condition fails, we stop. This leaves the array "partially sorted"
        // in cache-friendly clusters.
    }

    // Standard partition logic using Median-of-Three
    public static int  partition(int[] arr, int low, int high) {
        int mid = low + (high - low) / 2;
        // Basic median-of-three swap logic
        if (arr[mid] < arr[low]) swap(arr, low, mid);
        if (arr[high] < arr[low]) swap(arr, low, high);
        if (arr[mid] < arr[high]) swap(arr, mid, high);

        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
