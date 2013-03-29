package player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

/**
 * Parser test file
 * @author kimtoy
 *
 */
public class ParserTest {
    @Test
    public void testParser(){
        BufferedReader f = null;
        String line = "";
        try {
          f = new BufferedReader(new FileReader("sample_abc/fur_elise.abc"));
          String nextLine = f.readLine();
          while (nextLine != null) {
            line += nextLine + "\n";
            nextLine = f.readLine();
          }
          f.close();
        } catch (IOException e) {
          System.err.println("File couldn't be read");
        }
        System.out.println(line);
        
        Piece piece = Parser.parse(line);
        System.out.println("Default length "+piece.getDefaultNoteLength()+" meter "+piece.getMeter()+" tempo "+piece.getTempo()+" key "+piece.getKey());
    }
}
