# Project README: Improving Counting Sort via Data Locality

This project implements and evaluates the cache-friendly hybrid sorting algorithm proposed by Shamsed Mahmud,Sardar Anisul Haque, and Nazim Choudhury (2022). The goal is to demonstrate how preprocessing data with Quicksort can improve the performance of Counting Sort by inducing data locality.

## 1. Project Components
The implementation consists of the following Java classes:

* **Main.java**: The central test suite. It contains the logic to reproduce Tables 1, 2, and 3 from the paper and includes the `hybridSort` entry point.
* **QuickSortModified.java**: Implements "Algorithm 2" from the paper. This is a Quicksort variant that stops partitioning once a sub-array fits the cache threshold $C$.
* **CountingSort.java**: Implements the classic Counting Sort (Algorithm 1). It is used as a standalone baseline and as the final stage of the hybrid sort.
* **QuickSort.java**: Standard Quicksort using Median-of-Three pivoting (Baseline $T_1$).
* **QuickSortInsertion.java**: An optimized Quicksort that switches to Insertion Sort for small partitions (Baseline $T_2$).

---

## 2. Technical Implementation Details

### The Locality Threshold (C)
In `QuickSortModified.java`, the algorithm uses the following condition to stop recursion:
`if ((high - low) > 0 && (maxValue - minValue + (high - low)) > C)`
We have set **$C = 1000$**. This ensures that the combined size of the data and the auxiliary counting array fits within the CPU's cache, preventing "Capacity Cache Misses."

### Pivot Selection
All Quicksort implementations use **Median-of-Three** pivoting. This was chosen to prevent the $O(n^2)$ worst-case scenario on sorted or nearly-sorted data, ensuring the preprocessing phase remains efficient.

---

## 3. How to Reproduce Results

### Prerequisites
* Java Development Kit (JDK) 8 or higher.

### Execution Steps
1. Open a terminal/command prompt in the project folder.
2. Compile all files:
   `javac *.java`
3. Run the benchmarks:
   `java Main`

---

## 4. Understanding the Output

The program will output three sections corresponding to the paper's experiments:

1.  **Table 1 (Locality Test):** Compares Counting Sort on random vs. sorted data. You will observe that sorted data is significantly faster because of linear memory access.
2.  **Table 2 (Robustness Test):** Evaluates the "Worst Case" ($r \gg n$). It shows that the Hybrid Sort manages large ranges better than classic Counting Sort.
3.  **Table 3 (Final Comparison):** Compares all three algorithms.
    * **$T_1$**: Standard Quicksort
    * **$T_2$**: Quicksort + Insertion Sort
    * **$T_3$**: Our Hybrid (Modified Quicksort + Counting Sort)
    * *Observation:* $T_3$ should consistently show the lowest execution time.

---

## 5. Developer Notes for Documentation Team
* **JVM Warm-up:** The first test run may show slight variance due to JIT compilation. The results stabilize after the first iteration.
* **Precision:** We used `System.nanoTime()` to capture the millisecond decimals ($0.000$ ms) required to match the paper's data precision.
