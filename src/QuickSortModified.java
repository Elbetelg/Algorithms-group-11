import java.util.*;

public class QuickSortModified {
    public List<Partition> partitions = new ArrayList<>();

    public void quicksort_modified(int[] arr, int low, int high, int maxValue, int minValue, int C) {
        // Base case: single element is always a valid leaf partition
        if (low >= high) {
            if (low == high) partitions.add(new Partition(low, high, arr[low], arr[low]));
            return;
        }

        // Threshold check: if (range + size) <= C, this chunk is cache-friendly enough
        if ((maxValue - minValue + (high - low)) <= C) {
            partitions.add(new Partition(low, high, minValue, maxValue));
            return;
        }

        // 1. Median-of-Three Pivot Selection
        int pivotIndex = partition(arr, low, high);
        int midValue = arr[pivotIndex];

        // 2. Recursive calls for sub-partitions
        quicksort_modified(arr, low, pivotIndex - 1, midValue, minValue, C);
        quicksort_modified(arr, pivotIndex + 1, high, maxValue, midValue, C);
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
