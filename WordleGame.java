import java.util.Scanner;
import java.io.IOException;

/**
 * The WordleGame class exists only to as a place to store this main method!
 *
 */
public class WordleGame {
    public static void main(String[] args) throws IOException {

        // we'll limit the number of guesses with these variables.
        int maxGuesses = 10;
        int nGuesses = 0;

        // Here are two ways to create the puzzle. Using an array is good for testing
        // using the file is good for playing...
        String[] shortlist = {"raster"}; //{"state", "tacks"};
        Wordle puzzle = new Wordle(shortlist);
//        Wordle puzzle = new Wordle("norvig200.txt", 5, 100000, 0);

        puzzle.initGame();

        // The scanner will read from System.in (i.e., keyboard input), but we need a prompt
        // so people know to type.
        // I've used System.out.print (instead of println) so that you type on the same line
        // as the prompt.
        System.out.print("Your guess: ");
        Scanner scan = new Scanner(System.in);

        Hint h = null;
        while(scan.hasNext() && nGuesses < maxGuesses) {
            nGuesses += 1;
            String token = scan.next();
            System.out.println("  Got: '" + token + "'");
            try {
                h = puzzle.guess(token);
                h.write();
                if ( h.isWin() ) {
                    System.out.println("You won in " + nGuesses + " moves!");
                    break;
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("... try again...");
                nGuesses -= 1; // let's not count that guess!
            }
            // print the prompt for the next guess...
            System.out.print("Your guess: ");
        }
        if (!h.isWin()) {
            System.out.println("Too many moves!  You lose!");
        }

    }
}
