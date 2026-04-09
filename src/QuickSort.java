public class QuickSort {
    // 1. Rename this from 'swap' to 'sort'
    void sort(int a[], int l, int h) {
        if (l < h) {
            int pi = partition(a, l, h);
            sort(a, l, pi - 1);
            sort(a, pi + 1, h);
        }
    }

    int partition(int arr[], int low, int high) {
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

    // 2. Add this separate swap method
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}