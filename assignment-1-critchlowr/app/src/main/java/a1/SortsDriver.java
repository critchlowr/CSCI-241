package a1;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Ryan Critchlow
 */
//Prompts the user for array size and which sort to perform
//prints the array before and after sorting

public class SortsDriver {

    public static void main(String[] args) {
        char sortMethod = getSortMethod();
        int arraySize = getArraySize();

        //create random array of user input length
        int[] A = getRandomArray(arraySize, 13);
        //return unsorted array if <= than 20
        if (A.length <= 20) {
            System.out.println("Unsorted:");
            System.out.println(Arrays.toString(A));
        }
        //execute the actual sort method
        executeSort(sortMethod, A);
    }
    private static char getSortMethod () {
        //prompt user and grab input
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter sort (i[nsertion], q[uick], m[erge], r[adix], a[ll])");
        return userInput.next().charAt(0);
    }
    private static int getArraySize () {
        //prompt user and grab input
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter n (size of array to sort)");
        return userInput.nextInt();
    }
    private static int[] getRandomArray(int n, long seed) {
        int[] A = new int[n];
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(2 * n + 1) - n;
        }
        return A;
    }
    private static void executeSort (char sortMethod, int[] A) {
        //Constructor to call methods from Sorts
        Sorts Sorts = new Sorts();
        //execute sort
        String method;
        switch (sortMethod) {
            case 'i': method = "insertionSort:";
                Sorts.insertionSort(A, 0, A.length);
                break;
            case 'm': method = "mergeSort:";
                Sorts.mergeSort(A, 0, A.length);
                break;
            case 'q': method = "quickSort:";
                Sorts.quickSort(A, 0, A.length);
                break;
            case 'r': method = "radixSort:";
                Sorts.radixSort(A);
                break;
            case 'a': method = "all sort methods:";
                execAll(A);
                break;
            default: method = "defaulted insertionSort:";
                Sorts.insertionSort(A, 0, A.length);
                break;
        }
        //print results for anything that wasn't input as 'a' -- execAll(A) considers A
        if (sortMethod != 'a') {
            //return unsorted array if <= than 20
            if (A.length <= 20) {
                System.out.println("Sorted via " + method);
                System.out.println(Arrays.toString(A));
                System.out.println("Comparisons for " + method + " " + Sorts.getComparisonCount());
                Sorts.resetComparisonCount();
            } else {
                System.out.println("Sorted via " + method);
                System.out.println("Comparisons for " + method + " " + Sorts.getComparisonCount());
                Sorts.resetComparisonCount();
            }
        }
    }
    private static void execAll (int [] A) {
        Sorts Sorts = new Sorts();
        //Constructor to call methods from Sorts
        int[] copy1 = A.clone();
        int[] copy2 = A.clone();
        int[] copy3 = A.clone();

        //run insertion
        Sorts.insertionSort(A, 0, A.length);
        System.out.println("Comparisons for insertionSort " + Sorts.getComparisonCount());
        Sorts.resetComparisonCount();

        //run quick
        Sorts.quickSort(copy1, 0, A.length);
        System.out.println("Comparisons for quickSort " + Sorts.getComparisonCount());
        Sorts.resetComparisonCount();

        //run merge
        Sorts.mergeSort(copy2, 0, A.length);
        System.out.println("Comparisons for mergeSort " + Sorts.getComparisonCount());
        Sorts.resetComparisonCount();

        //run radix
        Sorts.radixSort(copy3);
        System.out.println("Comparisons for radixSort " + Sorts.getComparisonCount());
        Sorts.resetComparisonCount();

        //only need to print one of the sorted Arrays
        System.out.println("Sorted Array:");
        System.out.println(Arrays.toString(A));
    }
}
