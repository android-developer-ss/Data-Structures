/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svsmergesort2;

/**
 *
 * @author SnehalSutar
 */
public class SvsMergeSort2 {

    public static int h1, h2;

    /**
     * *************************************************************************
     * Divide array into 2 parts or if the size of array is less then 11 then
     * use insertion sort on the divided arrays and then call Merge procedure to
     * merge the two sorted arrays. 
    **************************************************************************
     */
    static int MergeSort(int[] A, int[] B, int p, int r) {
        int q;
        if (p < r) {
            q = (p + r) / 2;
            h1 = MergeSort(A, B, p, q);
            h2 = MergeSort(A, B, q + 1, r);
            Merge(A, B, p, q, r);

            if (h1 % 2 != 0) //h1 is odd
            {
                Merge(B, A, p, q, r);
            } else {
                Merge(A, B, p, q, r);
            }

            return h1 + 1;
        } else {
            return 0;
        }
    }

    /**
     * *************************************************************************
     * Merge the arrays using the method : Data alternates between A and B.
     * which is for 1st iteration the data is copied into array A while in 2nd
     * iteration the data is copied to array B. But in the final iteration the
     * data should be copied finally to array A. 
    **************************************************************************
     */
    static void Merge(int[] A, int[] B, int p, int q, int r) {

        //Copy left half of array from A to B.
        for (int i = p; i <= q; i++) {
            B[i] = A[i];
        }
        //Copy right half of array from A to B.
        for (int i = q + 1; i <= r; i++) {
            B[i] = A[i];
        }
        // Merge the arrays in sorted order back to A from the splitted B array.
        int i = p;
        int j = q + 1;
        for (int k = p; k <= r; k++) {
            if (j > r || (i <= q && B[i] <= B[j])) {
                A[k] = B[i++];
            } else {
                A[k] = B[j++];
            }
        }
    }

    /**
     * *************************************************************************
     * MAIN FUNCTION
     *
     * @param args contains the number of values with which merge sort is going
     * to be tested.
     *************************************************************************
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[] A = new int[n];
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = n - i;
        }
        long start = System.currentTimeMillis();
        MergeSort(A, B, 0, n - 1);
        long last = System.currentTimeMillis();

        for (int j = 0; j < A.length - 1; j++) {
            if (A[j] > A[j + 1]) {
                System.out.println("Sorting failed :-(");
                return;
            }
        }
        System.out.println("Success!");
        System.out.println(last - start);
    }

}
