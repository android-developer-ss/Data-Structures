/*******************************************************************************
 * Developed By: Snehal V Sutar. 
 * Net ID: svs130130
 * Class Name: GetBoardStateFile
 * Function: This class acts as Technical Service Layer. Reads the  file from
 *           the database which has the initial state of the 8 puzzle problem.
 ******************************************************************************/

package recursivebfs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Snehal
 */
public class GetBoardStateFile {
    
    public String readFile(){
         String str=null;
        try {            
            //Name of the File where the 8 puzzle board is stored locally.
            String filePath = "8PuzzleBoard.txt";
            
            //To open FILE for Reading StudentStruct file.
            BufferedReader bReader;
            bReader = new BufferedReader(new FileReader(filePath));
            String line;
            String boardVal[];
            boardVal = new String[9];
            //Declare a String to get the state of board.
            char stateOfBoard[] = new char[9];
            int strCount = 0;
            //Looping the read block until all lines in the file are read.
            while ((line = bReader.readLine()) != null) {
              boardVal = line.split("\t");
              stateOfBoard[strCount] = boardVal[0].toCharArray()[0];
              strCount++;
              stateOfBoard[strCount] = boardVal[1].toCharArray()[0];
              strCount++;
              stateOfBoard[strCount] = boardVal[2].toCharArray()[0];
              strCount++;
            }
            bReader.close();
            str = new String(stateOfBoard);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetBoardStateFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetBoardStateFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }
}
