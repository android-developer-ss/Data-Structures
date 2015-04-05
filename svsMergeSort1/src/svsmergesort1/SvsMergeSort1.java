/*******************************************************************************
 * Developed By: Snehal V Sutar. 
 * Net ID: svs130130
 * Class Name: SvsMergeSort1
 * Function: 2. Create a single auxiliary array B in main and pass it to 
 *              MergeSort and Merge. In each instance of Merge, data is copied 
 *              from A to B and merged back into A.
 ******************************************************************************/
package svsmergesort1;

/**
 *
 * @author SnehalSutar
 */
public class SvsMergeSort1 {

    /***************************************************************************
    Divide array into 2 parts or if the size of array is less then 11 then use 
    insertion sort on the divided arrays and then call Merge procedure to 
    merge the two sorted arrays. 
    ***************************************************************************/
    static void MergeSort(int[] A,int[] B, int p, int r) {
        if (p < r) {
	    if (r-p > 11) {
                int q = (p+r)/2;
                MergeSort(A, B, p, q);
                MergeSort(A, B, q+1, r);
                Merge(A, B, p, q, r);
            } else {  // Insertion sort
		for(int i=p, j=i; i<r; j=++i) {
		    int ai = A[i+1];
		    while(ai < A[j]) {
			A[j+1] = A[j];
			if (j-- == p) {
			    break;
			}
		    }
		    A[j+1] = ai;
	 	}
	    }
	}
    }

    /***************************************************************************
    * Merge the arrays using the method : In each instance of Merge, data is 
    * copied from A to B and merged back into A.
    * which means left half of A is copied to B and right half of A is copied to
    * B separately and then merged back into A.
    ***************************************************************************/
    static void Merge(int[] A,int[] B, int p, int q, int r) {
        
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
    
    /***************************************************************************
     * MAIN FUNCTION
     * @param args contains the number of values with which merge sort is going 
     * to be tested.
     **************************************************************************/
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[] A = new int[n];
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = n-i;
        }
	long start = System.currentTimeMillis();
        MergeSort(A,B, 0, n-1);
	long last = System.currentTimeMillis();

        for (int j = 0; j < A.length-1; j++) {
            if(A[j] > A[j+1]) {
		System.out.println("Sorting failed :-(");
		return;
	    }
        }
	System.out.println("Success!");
	System.out.println(last-start);
    }
    }
    

