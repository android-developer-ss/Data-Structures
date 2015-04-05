/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Snehal
 */
public class Project15 {

    //Global variables for knuth_morris_pratt_algo.
    private static int[] failure;

    //Global variables for rabin_karp_algo
    private static String pat;
    private static String space = " ";
    private static long patHash;     // pattern hash value
    private static long prime_num;   // a large prime, small enough to avoid long overflow
    private static long radix_prime; // radix^(pattern_len-1) % prime_num
    private static int pat_len;     // pattern length
    private static int radix;       // radix

    private static long start_time, end_time;
    private static long naive_time, rk_time, km_time, bm_time;

    private static ArrayList<Integer> index_list = new ArrayList<>();

    /**
     * *************************************************************************
     * Naive search algorithm.
     *
     * @param orig_str
     * @param pattern
     * **************************************************************************
     */
    private static void naive_search(String orig_str, String pattern) {
        int pattern_len = pattern.length();
        int orig_str_len = orig_str.length();
        start_time = System.currentTimeMillis();
        /* A loop to slide pat[] one by one */
        for (int i = 0; i <= orig_str_len - pattern_len; i++) {
            int j;
            /* For current index i, check for pattern match */
            for (j = 0; j < pattern_len; j++) {
                if (orig_str.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == pattern_len) // if pat[0...pattern_len-1] = txt[i, i+1, ...i+pattern_len-1]
            {
//                System.out.println("Naive Pattern found at index: " + i);
                index_list.add(i);
            }
        }
        end_time = System.currentTimeMillis();
        naive_time = end_time - start_time;
    }

    /**
     * **************************************************************************
     * In computer science, the Rabin–Karp algorithm or Karp–Rabin algorithm is
     * a string searching algorithm created by Richard pattern_len Karp and
     * Michael O Rabin (1987) that uses hashing to find any one of a set of
     * pattern strings in a text For text of length n and p patterns of combined
     * length m, its average and best case running time is O(n+m) in space O(p),
     * but its worst-case time is O(nm) .
     *
     * @param orig_str
     * @param pattern
     * **********************************************************************
     */
    public static void rabin_karp_algo(String orig_str, String pattern) {
        //Local variable declaration.
        int offset;
        //----------------------------------------------------------------------
        start_time = System.currentTimeMillis();

        pat = pattern;
        radix = 256;
        pat_len = pat.length();
        prime_num = longRandomPrime();

        // precompute radix^(pattern_len-1) % prime_num for use in removing leading digit
        radix_prime = 1;
        for (int i = 1; i <= pat_len - 1; i++) {
            radix_prime = (radix * radix_prime) % prime_num;
        }
        patHash = hash(pat, pat_len);

        offset = search(orig_str);

        // print results
//        System.out.println("Orig:    " + orig_str);
        // from brute force search method 1
//        System.out.print("pattern: ");
//        for (int i = 0; i < offset; i++) {
//            System.out.print(" ");
//        }
//        System.out.println(pat);
        end_time = System.currentTimeMillis();
        rk_time = end_time - start_time;
    }

    // Compute hash for key[0..pattern_len-1]. 
    private static long hash(String key, int M) {
        long h = 0;
        for (int j = 0; j < M; j++) {
            h = (radix * h + key.charAt(j)) % prime_num;
        }
        return h;
    }

    // Las Vegas version: does pat[] match txt[i..i-pattern_len+1] ?
    private static boolean check(String txt, int i) {
        for (int j = 0; j < pat_len; j++) {
            if (pat.charAt(j) != txt.charAt(i + j)) {
                return false;
            }
        }
        return true;
    }

    // Check for exact match
    public static int search(String txt) {
        int txt_len = txt.length();
        int count = 0;
        if (txt_len < pat_len) {
            return txt_len;
        }
        long txtHash = hash(txt, pat_len);

        // check for match at offset 0
        if ((patHash == txtHash) && check(txt, 0)) {
            count++;
//            return 0;
        }

        // check for hash match; if hash match, check for exact match
        for (int i = pat_len; i < txt_len; i++) {
            // Remove leading digit, add trailing digit, check for match. 
            txtHash = (txtHash + prime_num - radix_prime * txt.charAt(i - pat_len) % prime_num) % prime_num;
            txtHash = (txtHash * radix + txt.charAt(i)) % prime_num;

            // match
            int offset = i - pat_len + 1;
            if ((patHash == txtHash) && check(txt, offset)) {
                count++;
//                return offset;
            }
        }

        // no match
        if (count == 0) {
            return txt_len;
        } else {
            return 0;
        }
    }

    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    /**
     * **************************************************************************
     * Knuth–Morris–Pratt string searching algorithm (or KMP algorithm) searches
     * for occurrences of a "word" W within a main "text string" S by employing
     * the observation that when a mismatch occurs, the word itself embodies
     * sufficient information to determine where the next match could begin,
     * thus bypassing re-examination of previously matched characters.
     *
     * @param orig_str
     * @param pat
     * ************************************************************************
     */
    public static void knuth_morris_pratt_algo(String orig_str, String pat) {
        int i = 0, j = 0;
        int count = 0;
        int orig_str_len = orig_str.length();
        int pattern_len = pat.length();
        int pos;
        //----------------------------------------------------------------------
        start_time = System.currentTimeMillis();
        /**
         * pre construct failure array for a pattern *
         */
        failure = new int[pat.length()];
        //----------------------------------------------------------------------
        //update failure.
        failure[0] = -1;
        for (int a = 1; a < pattern_len; a++) {
            int b = failure[a - 1];
            while ((pat.charAt(a) != pat.charAt(b + 1)) && b >= 0) {
                b = failure[b];
            }
            if (pat.charAt(a) == pat.charAt(b + 1)) {
                failure[a] = b + 1;
            } else {
                failure[a] = -1;
            }
        }
        //----------------------------------------------------------------------
        //find match 
//        int i_len=0;
        while (i < orig_str_len-1) {
            j = 0;
            while (i < orig_str_len && j < pattern_len) {
                if (orig_str.charAt(i) == pat.charAt(j)) {
                    i++;
                    j++;
                } else if (j == 0) {
                    i++;
                } else {
                    j = failure[j - 1] + 1;
                }
            }
            pos = ((j == pattern_len) ? (i - pattern_len) : -1);
            if (pos != -1) {
                count++;
            }
            i--;
        }

//        if (pos == -1) {
//            System.out.println("\nNo match found");
//        } else {
//            System.out.println("\nMatch found at index " + pos);
//        }
        end_time = System.currentTimeMillis();
        km_time = end_time - start_time;
    }

    /**
     * *************************************************************************
     * The algorithm preprocesses the string being searched for (the pattern),
     * but not the string being searched in (the text) It is thus well-suited
     * for applications in which the pattern is much shorter than the text or
     * does persist across multiple searches The Boyer-Moore algorithm uses
     * information gathered during the preprocess step to skip sections of the
     * text, resulting in a lower constant factor than many other string
     * algorithms In general, the algorithm runs faster as the pattern length
     * increases.
     *
     * @param orig_str
     * @param pattern
     * **************************************************************************
     */
    private static void boyer_moore_algo(String orig_str, String pat) {

        start_time = System.currentTimeMillis();
        char[] text = orig_str.toCharArray();
        char[] pattern = pat.toCharArray();
        int pos;// = indexOf(text, pattern);
        //----------------------------------------------------------------------
        
        int count =0;
        if (pattern.length == 0) {
            pos= 0;
        }
        int charTable[] = makeCharTable(pattern);
        int offsetTable[] = makeOffsetTable(pattern);
        for (int i = pattern.length - 1, j; i < text.length;) {
            for (j = pattern.length - 1; pattern[j] == text[i]; --i, --j) {
                if (j == 0) {
                    count++;
                    break;
//                    return i;
                }
            }

            // i += pattern.length - j; // For naive method
            i += Math.max(offsetTable[pattern.length - 1 - j], charTable[text[i]]);
        }
//        if (pos == -1) {
//            System.out.println("\nNo Match\n");
//        } else {
//            System.out.println("Pattern found at position : " + pos);
//        }

        end_time = System.currentTimeMillis();
        bm_time = end_time - start_time;
    }

    
    /**
     * Makes the jump table based on the mismatched character information *
     */
    private static int[] makeCharTable(char[] pattern) {
        final int ALPHABET_SIZE = 256;
        int[] table = new int[ALPHABET_SIZE];
        for (int i = 0; i < table.length; ++i) {
            table[i] = pattern.length;
        }
        for (int i = 0; i < pattern.length - 1; ++i) {
            table[pattern[i]] = pattern.length - 1 - i;
        }
        return table;
    }

    /**
     * Makes the jump table based on the scan offset which mismatch occurs. *
     */
    private static int[] makeOffsetTable(char[] pattern) {
        int[] table = new int[pattern.length];
        int lastPrefixPosition = pattern.length;
        for (int i = pattern.length - 1; i >= 0; --i) {
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPosition = i + 1;
            }
            table[pattern.length - 1 - i] = lastPrefixPosition - i + pattern.length - 1;
        }
        for (int i = 0; i < pattern.length - 1; ++i) {
            int slen = suffixLength(pattern, i);
            table[slen] = pattern.length - 1 - i + slen;
        }
        return table;
    }

    /**
     * function to check if needle[p:end] a prefix of pattern *
     */
    private static boolean isPrefix(char[] pattern, int p) {
        for (int i = p, j = 0; i < pattern.length; ++i, ++j) {
            if (pattern[i] != pattern[j]) {
                return false;
            }
        }
        return true;
    }

    /**
     * function to returns the maximum length of the substring ends at p and is
     * a suffix *
     */
    private static int suffixLength(char[] pattern, int p) {
        int len = 0;
        for (int i = p, j = pattern.length - 1; i >= 0 && pattern[i] == pattern[j]; --i, --j) {
            len += 1;
        }
        return len;
    }

    /**
     * *************************************************************************
     * MAIN FUNCTION
     *
     * @param args the command line arguments
     * *************************************************************************
     */
    public static void main(String[] args) {

        String orig_str = "abcd efefgghijkl";
        String pattern = "efg";

        pattern = args[0];
        //----------------------------------------------------------------------
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
//                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String everything = sb.toString();
                orig_str = everything;
            } finally {
                br.close();
            }
        } catch (IOException ioe) {

        }
        //----------------------------------------------------------------------
        //Call Rabin–Karp algorithm.
        naive_search(orig_str, pattern);
        //----------------------------------------------------------------------
        //Call Rabin–Karp algorithm.
        rabin_karp_algo(orig_str, pattern);
        //----------------------------------------------------------------------
        //Call Rabin–Karp algorithm.
        knuth_morris_pratt_algo(orig_str, pattern);
        //----------------------------------------------------------------------
        //Call Boyer–Moore string search algorithm
        boyer_moore_algo(orig_str, pattern);

        System.out.println(index_list.size() + space + naive_time 
                + space + rk_time 
                + space + km_time 
                + space + bm_time );
        if (index_list.size() <= 20) {
            System.out.print(index_list);
        }
    }

}
