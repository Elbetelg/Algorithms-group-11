# Project README: Improving Counting Sort via Data Locality

This project implements and evaluates the cache-friendly hybrid sorting algorithm proposed by Shamsed Mahmud, Sardar Anisul Haque, and Nazim Choudhury (2022). The goal is to demonstrate how preprocessing data with Quicksort can improve the performance of Counting Sort by inducing data locality.

## 1. Project Components
The implementation consists of the following Java classes:

* **Main.java**: The central test suite. It reproduces Tables 1, 2, and 3 from the paper.
* **QuickSortModified.java**: Implements "Algorithm 2" (Preprocessing). Stops partitioning once a sub-array fits the cache threshold $C$.
* **CountingSort.java**: Implements the classic Counting Sort (Algorithm 1).
* **QuickSort.java**: Standard Quicksort baseline ($T_1$).
* **QuickSortInsertion.java**: Quicksort optimized with Insertion Sort baseline ($T_2$).

---

## 2. Technical Implementation Details

### The Locality Threshold (C)
In `QuickSortModified.java`, the algorithm uses the condition:
`if ((high - low) > 0 && (maxValue - minValue + (high - low)) > C)`
We used **$C = 1000$** as per the paper's findings to ensure sub-arrays fit into the CPU cache levels.

### Pivot Selection
All Quicksort variants use **Median-of-Three** pivoting to ensure consistent $O(n \log n)$ preprocessing and prevent performance degradation on sorted inputs.

---

## 3. How to Reproduce Results

### Prerequisites
* Java Development Kit (JDK) 8 or higher.

### Execution Steps
1. Open a terminal in the project folder.
2. Compile all files: `javac *.java`
3. Run the benchmarks: `java Main`

**Note on Methodology:** To ensure reliability, the program performs a **Warm-up Phase** of 10 runs before measuring, and every result printed is an **Average of 10 Trials**.

---

## 4. Understanding the Output

1. **Table 1 (Locality Test):** Demonstrates that "Sorted" data is ~2-3x faster than "Random" data due to increased cache hits.
2. **Table 2 (Robustness Test):** Evaluates the "Worst Case" ($r \gg n$). Shows the Hybrid Sort's stability when ranges are large.
3. **Table 3 (Final Comparison):** The core result. $T_3$ (Hybrid) should consistently outperform $T_1$ and $T_2$, proving the efficiency of the locality-based approach.

---

## 5. Developer Notes for Documentation Team
* **Warm-up Loop:** We implemented a 10-iteration warm-up to trigger the JVM's Just-In-Time (JIT) compiler, ensuring the CPU is at peak performance before benchmarking.
* **Averaging:** We used `NUM_TRIALS = 10` to filter out system "noise" (background tasks), making our T-values more statistically significant.
* **Precision:** We used `System.nanoTime()` divided by `1,000,000.0` to maintain the 3-decimal precision found in the original research tables.