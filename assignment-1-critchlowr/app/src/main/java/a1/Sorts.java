package a1;

import java.util.*;

/**
 * @author Ryan Critchlow
 */
public class Sorts {

    // maintains a count of comparisons performed by this Sorts object
    private int comparisonCount;

    public int getComparisonCount() {
        return comparisonCount;
    }

    public void resetComparisonCount() {
        comparisonCount = 0;
    }
    /**
     * Sorts A[start..end] in place using insertion sort
     * Precondition: 0 <= start <= end <= A.length
     */
    public void insertionSort(int[] A, int start, int end) {
        // j is used to compare values against their neighbor
        int j, i;
        // start should come in as zero
        // end comes in as A.length
        for (i = start + 1; i < end; i++) {
            j = i;
            while (j > 0 && (A[j - 1] > A[j])) {
                swap(A, j, j - 1);
                j--;
                comparisonCount++;
            }
        }
    }

    /**
     * Partitions A[start..end] around the pivot A[pivIndex]; returns the
     * pivot's new index.
     * Precondition: start <= pivIndex < end
     * Post-condition: If partition returns i, then
     * A[start..i] <= A[i] <= A[i+1..end]
     **/
    public int partition(int[] A, int start, int end, int pivIndex) {
        //when the pivot was not first swapped to the end of the array
        //the loop logic would work 50% of the time for test case scenarios
        //by forcing my pivot to the end of the array I could more easily control where
        //it would be at the final swap outside the loop
        swap(A, pivIndex, end - 1);
        pivIndex = end - 1;

        int i = start - 1;
        for (int j = start; j < end; j++) {
            // If current element is < pivot
            // swap to be to the left of the pivot
            if (A[j] < A[pivIndex]) {
                comparisonCount++;
                // increment our pointer and swap that location with j
                i++;
                swap(A, i, j);
            }
        }
        //after our loop criteria terminates
        //this will move the pivot to its correct location
        swap(A, pivIndex, i + 1);
        return (i + 1);
    }

    /**
     * use quicksort to sort the sub-array A[start..end]
     */
    public void quickSort(int[] A, int start, int end) {
        if (start < end) {
            int pivIndex = end - 1;
            //initiate the partition
            int mid = partition(A, start, end, pivIndex);
            //recursively do left of the pivot
            quickSort(A, start, mid);
            //recursively do right of the pivot
            quickSort(A, mid + 1, end);
        }
    }

    /**
     * merge the sorted sub-arrays A[start..mid] and A[mid..end] into
     * a single sorted array in A.
     */
    public void merge(int[] A, int start, int mid, int end) {
        //copy left and right into their own arrays
        int[] left = Arrays.copyOfRange(A, start, mid);
        int[] right = Arrays.copyOfRange(A, mid, end);
        //left and right position
        int l = mid - start;
        int r = end - mid;
        //initialize counters
        int i = 0, j = 0, k = start;
        //so long as the bounds of left and right aren't reached
        while (i < l && j < r) {
            //when left is smaller than the right
            //put left position into original array
            if (left[i] <= right[j]) {
                comparisonCount++;
                A[k++] = left[i++];
            }
            // otherwise place right position into original array
            else {
                comparisonCount++;
                A[k++] = right[j++];
            }
        }
        //loop broken
        //copy over remaining elements in left and right into main array
        while (i < l) {
            A[k++] = left[i++];
        }
        while (j < r) {
            A[k++] = right[j++];
        }
    }

    /**
     * use mergesort to sort the sub-array A[start..end]
     */
    public void mergeSort(int[] A, int start, int end) {
        // Check if array A is large enough to split
        // Then create two sub-arrays for the left and right portions
        if ((end - start) < 2) {
            return;
        }
            int mid = start + (end - start) / 2;
            //recursively sort the left
            mergeSort(A, start, mid);
            //recursively sort the right
            mergeSort(A, mid, end);
            //combine the two sub-arrays
            merge(A, start, mid, end);
    }

    /**
     * Sort A using LSD radix sort. Uses counting sort to sort on each digit
     */
    public void radixSort(int[] A) {
        //initialize trackers
        int min = 0;
        int max = 0;
        int start = 0;
        int end = A.length - 1;

        //apply countSort for the maximum amount of possible passes for an integer - 11
        for (int range = 0;  range < 11; range++) {
            countSort(A, start, end, range);
        }
    }

    private void countSort (int [] A, int start, int end, int range) {
        //initialized count array with 19 positions
        //we will later add a bias of 9 to our digits to account for negative values
        int[] bucket = new int[19];
        int[] output = new int[A.length];
        //loop through our input array
        //grab the digit of the element
        for (int i = start; i <= end; i++) {
            //grab digit - if equal to i then increment "bucket"
            //add 9 to the digit to make it "positive"
            bucket[getDigit(A[i], range) + 9]++;
        }
        //sum the bucket results
        for (int i = 1; i < 19; i++) {
            bucket[i] += bucket[i - 1];
        }
        //grab the digit of our value in array A
        //search bucket for that elemental position = bucket[arrDigit]
        //subtract one from the bucketPos so that A[i] in the output array is in right spot
        //decrement the value in bucket for our referenced element
        for (int i = end; i >= 0; i--) {
            //add 9 to the digit to make it positive
            int arrDigit = getDigit(A[i], range) + 9;
            int bucketPos = bucket[arrDigit];
            bucketPos = bucketPos - 1;
            output[bucketPos] = A[i];
            bucket[arrDigit]--;
        }
        //copy output back into A[]
        if (end + 1 - start >= 0) {
            System.arraycopy(output, start, A, start, end + 1 - start);
        }
    }

    //These are the provided helper methods

    /* return the 10^d's place digit of n */
    private int getDigit(int n, int d) {
        return (n / ((int) Math.pow(10, d))) % 10;
    }

    /**
     * swap a[i] and a[j]
     * pre: 0 <= i, j < a.size
     * post: values in a[i] and a[j] are swapped
     */
    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

}
