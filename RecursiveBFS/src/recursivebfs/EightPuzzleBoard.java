/**
 *******************************************************************************
 * Developed By: Snehal V Sutar. Net ID: svs130130 Class Name: EightPuzzleBoard
 * Function: Contains all the properties of the state of board in the 8 puzzle
 * problem.
 * *****************************************************************************
 */
package recursivebfs;

/**
 *
 * @author Snehal
 */
public class EightPuzzleBoard {

    private int fFuncVal, gFuncVal, hFuncVal;
    private StringBuilder stateofBoard;
    private int xEmptyTile, yEmptyTile;
    private int directionIndex;

    /**
     ***************************************************************************
     * Values are assigned to the board.
     * *************************************************************************
     */
    public void setStateofBoard(StringBuilder stateofBoard) {
        this.stateofBoard = stateofBoard;

    }

    public StringBuilder getStateofBoard() {

        return stateofBoard;

    }

    /**
     ***************************************************************************
     * Assign/store the path.
     * *************************************************************************
     */
    public void setDirectionIndex(int directionIndex) {

        this.directionIndex = directionIndex;

    }

    public int getDirectionIndex() {

        return directionIndex;

    }

    /**
     ***************************************************************************
     * Set the X Position of the Blank Tile.
     * *************************************************************************
     */
    public void setBlankXPos(int xEmptyTile) {

        this.xEmptyTile = xEmptyTile;
    }

    public int getBlankXPos() {

        return xEmptyTile;
    }

    /**
     ***************************************************************************
     * Set the Y Position of the Blank Tile.
     * *************************************************************************
     */
    public void setBlankYPos(int yEmptyTile) {

        this.yEmptyTile = yEmptyTile;
    }

    public int getBlankYPos() {

        return yEmptyTile;
    }

    /**
     ***************************************************************************
     * Set the F function value which is G value + H value.
     * *************************************************************************
     */
    public void setFFuncVal(int fVal) {

        this.fFuncVal = fVal;
    }

    public int getFFuncVal() {

        return fFuncVal;
    }

    /**
     ***************************************************************************
     * Set the G function value which is the depth of the tree while solving the
     * 8 puzzle problem.
     * *************************************************************************
     */
    public void setGFuncVal(int gVal) {

        this.gFuncVal = gVal;
    }

    public int getGFuncVal() {

        return gFuncVal;
    }

    /**
     ***************************************************************************
     * Set the H function value which is the heauristic value i.e. the Manhattan
     * distance of the function.
     * *************************************************************************
     */
    public void setHFuncVal(int hVal) {

        this.hFuncVal = hVal;

    }

    public int getHFuncVal() {

        return hFuncVal;
    }

}
