/**
 * *****************************************************************************
 * Developed By: Snehal V Sutar. 
 * Net ID: svs130130 
 * Class Name: RecursiveCallRBFS
 * Function: This class contains the actual method to solve the 8 puzzle problem
 *           recursively. 
 *******************************************************************************
 */
package recursivebfs;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Snehal
 */
public class RecursiveCallRBFS {

    public static FinalResult RBFS(EightPuzzleBoard currState, int maxFVal,
            FinalResult finalRes) {

        //Local Objct Declaration.
        FinalResult finResTemp = new FinalResult();
        EightPuzzleBoard succState;
        ArrayList<EightPuzzleBoard> successorStates = new ArrayList<EightPuzzleBoard>();
        //----------------------------------------------------------------------
        RecursiveBFS.storePath[currState.getGFuncVal()] = currState.getDirectionIndex();
        
        
        //Direction matrix to move the Blank tile once in UP direction, LEFT
        //direction, DOWN direction and RIGHT direction.
        int[][] direction = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        //Check if the depth of search tree is going beyond 15 levels.
        
        if(currState.getGFuncVal() > 15){
            System.out.println("Search level going beyond 15 levels.");
            RecursiveBFS.EXIT_FLAG = 1;
            System.exit(0);
            return finResTemp;            
        }
        //----------------------------------------------------------------------
        // Check if the Heuristic value returned by Manhattan distance 
        // calculation is 0. If it is 0 then the GOAL state of the 8 puzzle
        // problem has been reached and we can print the moves of the blank tile
        if (currState.getHFuncVal() == 0) {//if GOAL STATE reached.

            finResTemp.maxFVal = maxFVal;
            finResTemp.solutionStateFound = currState;
            String sol = "uldr";

            // Print solution
            for (int i = 1; i <= currState.getGFuncVal(); i++) {
                switch (sol.charAt(RecursiveBFS.storePath[i])) {
                    case ('u'):
                        System.out.println("up");
                        break;
                    case ('d'):
                        System.out.println("down");
                        break;
                    case ('l'):
                        System.out.println("left");
                        break;
                    case ('r'):
                        System.out.println("right");
                        break;
                }
            }

            System.out.println("");

            return finResTemp;
        }

        //----------------------------------------------------------------------
        // If the puzzle is not yet solved  we need to calculate the next move 
        // of the puzzle. We do this by calculating the succcessor states of the
        // tree. We move the tile in all the 4/3/2 direction(valid move) and 
        // calculate the manhattan distance for each move. 
        for (int i = 0; i < 4; i++) {
            if (currState.getGFuncVal() != 0) {
                if (Math.abs(i - currState.getDirectionIndex()) == 2) {
                    continue;
                }
            }
            succState = computeSuccState(currState, direction[i][0], direction[i][1], i);

            //check if the state of board is solvable.
            if (RecursiveBFS.validMove == 0) {
                continue;
            }
            successorStates.add(succState);
        }
        //----------------------------------------------------------------------
        // Check if No successor found.
        if (successorStates.isEmpty()) {

            finResTemp.solutionStateFound = null;
            finResTemp.maxFVal = currState.getFFuncVal();
            return finResTemp;
        }

        //----------------------------------------------------------------------
        // Sort the successor in ascending order, select the bestNextState state
        // by selecting the state with lowest heuristic value and save the next 
        // bestNextState move.
        for (;;) {

            int alternative = 0;

            // sort successorStates according to their f-values in ascending
            Collections.sort(successorStates, new StateEightPuzzleComparator());

            EightPuzzleBoard bestNextState = successorStates.get(0);

            if (bestNextState.getFFuncVal() > maxFVal) {

                finResTemp.solutionStateFound = null;
                finResTemp.maxFVal = bestNextState.getFFuncVal();
                return finResTemp;
            }

            if (successorStates.size() > 1) {
                alternative = successorStates.get(1).getFFuncVal();
            } else {
                alternative = Integer.MAX_VALUE;
            }

            maxFVal = maxFVal < alternative ? maxFVal : alternative;
            finResTemp = RBFS(bestNextState, maxFVal, finalRes);

            successorStates.get(0).setFFuncVal(finResTemp.maxFVal);

            maxFVal = finResTemp.maxFVal;

            if (finResTemp.solutionStateFound != null) {
                return finResTemp;
            }

        }

    }

    /**
     * *************************************************************************
     * This function calculates the successor node with left, right, top or
     * bottom move of the blank space, calculates the heuristic value, g value
     * and total f value of the state of board.
     *
     * @param fatherNode
     * @param xMove
     * @param yMove
     * @param directionIndex
     * @return 
     ***************************************************************************
     */
    public static EightPuzzleBoard computeSuccState(EightPuzzleBoard fatherNode,
            int xMove, int yMove, int directionIndex) {

        int nextXCordiante, nextYCordiante;
        nextXCordiante = fatherNode.getBlankXPos();
        nextYCordiante = fatherNode.getBlankYPos();

        EightPuzzleBoard childNode = new EightPuzzleBoard();

        if ((nextXCordiante + xMove) < 0 || (nextYCordiante + yMove) < 0
                || (nextXCordiante + xMove) > (RecursiveBFS.BoardMatrixLen - 1)
                || (nextYCordiante + yMove) > (RecursiveBFS.BoardMatrixLen - 1)) {
            RecursiveBFS.validMove = 0;
            return childNode;

        }

        int nextEmptyTile = (nextXCordiante + xMove) * RecursiveBFS.BoardMatrixLen
                + (nextYCordiante + yMove);

        StringBuilder s1 = new StringBuilder(fatherNode.getStateofBoard());
        char ch = s1.charAt(nextEmptyTile);
        s1.setCharAt(nextEmptyTile, '0');
        s1.setCharAt(RecursiveBFS.BoardMatrixLen * nextXCordiante + nextYCordiante, ch);

        childNode.setStateofBoard(s1);
        childNode.setBlankXPos(nextXCordiante + xMove);
        childNode.setBlankYPos(nextYCordiante + yMove);
        childNode.setGFuncVal(fatherNode.getGFuncVal() + 1);
        childNode.setHFuncVal(CalculateManhattanDist.getManhattanDistance(s1));
        childNode.setDirectionIndex(directionIndex);

        int maxfValue = (fatherNode.getFFuncVal()) > (childNode.getGFuncVal() + childNode
                .getHFuncVal()) ? fatherNode.getFFuncVal() : (childNode.getGFuncVal() + childNode
                .getHFuncVal());
        childNode.setFFuncVal(maxfValue);
        RecursiveBFS.validMove = 1;
        return childNode;

    }

    /**
     * *************************************************************************
     *
     * Comparator class used to sort Nodes of Eight Puzzle in ascending order
     * with respect to their f-values
     *
     ***************************************************************************
     */
    static class StateEightPuzzleComparator implements Comparator<EightPuzzleBoard> {

        @Override
        public int compare(EightPuzzleBoard s1, EightPuzzleBoard s2) {
            return s1.getFFuncVal() - s2.getFFuncVal();
        }
    }
    /**
     * *************************************************************************
     */
}
