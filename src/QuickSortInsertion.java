public class QuickSortInsertion {
    private static final int INSERTION_THRESHOLD = 16; // Standard threshold for insertion sort

    public void sort(int[] arr, int low, int high) {
        if (low < high) {
            // If the size is small, use Insertion Sort
            if (high - low < INSERTION_THRESHOLD) {
                insertionSort(arr, low, high);
            } else {
                int pi = partition(arr, low, high);
                sort(arr, low, pi - 1);
                sort(arr, pi + 1, high);
            }
        }
    }

    private int partition(int[] arr, int low, int high) {
        int mid = low + (high - low) / 2;
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

    private void insertionSort(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}