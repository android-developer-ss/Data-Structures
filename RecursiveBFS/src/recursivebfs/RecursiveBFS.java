/**
 * *****************************************************************************
 * Developed By: Snehal V Sutar. 
 * Net ID: svs130130 
 * Class Name: RecursiveBFS
 * Function: This is the Main Class the starting point of the 8 puzzle problem,
 * which calls the reading of the file as well as calls the Recursive Best First
 * Search in order to solve the problem.
 * *****************************************************************************
 */
package recursivebfs;

/**
 *
 * @author Snehal
 */
public class RecursiveBFS {

    static int validMove;
    static int[] storePath = new int[10000];
    static public int BoardMatrixLen = 3;
    static public int EXIT_FLAG = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int blankSquarePos = 0;

        //Read the input file and put the appropriate values in the state board.
        GetBoardStateFile getBoard = new GetBoardStateFile();
        String stateOfBoard = getBoard.readFile();
        StringBuilder sb_stateOfBoard = new StringBuilder(stateOfBoard);
        //Get the blank space tile index.
        for (int counter = 0; counter < stateOfBoard.length(); counter++) {
            if (sb_stateOfBoard.charAt(counter) == '0') {
                blankSquarePos = counter;
            }
        }

        //Initialize the 8 Puzzle problem using the state of board recieved from 
        //above step.
        EightPuzzleBoard initialBoard = new EightPuzzleBoard();
        initialBoard.setStateofBoard(sb_stateOfBoard);
        initialBoard.setGFuncVal(0);
        initialBoard.setHFuncVal(CalculateManhattanDist.getManhattanDistance(sb_stateOfBoard));
        initialBoard.setFFuncVal(initialBoard.getHFuncVal() + initialBoard.getGFuncVal());
        initialBoard.setBlankXPos(blankSquarePos / BoardMatrixLen);
        initialBoard.setBlankYPos(blankSquarePos % BoardMatrixLen);
        initialBoard.setDirectionIndex(5);

        int flimit = initialBoard.getFFuncVal();
        FinalResult result = new FinalResult();
        result.maxFVal = 0;
        result.solutionStateFound = initialBoard;

        // call RBFS
        FinalResult resTemp = RecursiveCallRBFS.RBFS(initialBoard, Integer.MAX_VALUE, result);
    }

}
