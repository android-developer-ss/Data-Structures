/**
 * *****************************************************************************
 * Developed By: Snehal V Sutar. Net ID: svs130130 Class Name:
 * CalculateManhattanDist Function: Calculates the Manhattan distance i.e. the
 * total number of misplaced tiles in the state board of 8 puzzle problem.
 *****************************************************************************
 */
package recursivebfs;

/**
 *
 * @author Snehal
 */
public class CalculateManhattanDist {

    public static int getManhattanDistance(StringBuilder knightBoard) {

        int manhattanVal = 0;

        for (int i = 0; i < RecursiveBFS.BoardMatrixLen; i++) {
            for (int j = 0; j < RecursiveBFS.BoardMatrixLen; j++) {
                int pos = i * RecursiveBFS.BoardMatrixLen + j;
                String valStr = (Character.toString(knightBoard.charAt(pos)));
                int val = Integer.parseInt(valStr);
                int posxy[];// = new int[2];
                if (val == 0) {
                    continue;
                }

                posxy = getPositionOf(val);
                //Calculate the manhatten disctance. Num of misplaced tiles.
                int xTileDis = Math.abs(i - posxy[0]);
                int yTileDis = Math.abs(j - posxy[1]);

                manhattanVal = manhattanVal + (xTileDis + yTileDis);
            }
        }

        return manhattanVal;
    }

    /**
     ***************************************************************************
     * Function: Return the values (square under consideration) position in the
     * goal state.
     *
     * @param val
     * @return 
     ***************************************************************************
     */
    public static int[] getPositionOf(int val) {
        int posxy[] = new int[2];
        int actVal[][] = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};

        for (int i = 0; i < RecursiveBFS.BoardMatrixLen; i++) {
            for (int j = 0; j < RecursiveBFS.BoardMatrixLen; j++) {
                if (val == actVal[i][j]) {
                    posxy[0] = i;
                    posxy[1] = j;
                }
            }
        }
        return posxy;
    }
    /**
     * *************************************************************************
     */
}
