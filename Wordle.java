import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The puzzle game 'Wordle'!
 *
 * Basic idea:
 * the game loads in a set of 'known words' from a file or an array.
 * All 'known words' must have the same length (e.g., 5 characters)
 *
 * When the game starts, a secret word is chosen from the list of known words.
 * The player can then issue guesses.
 * Each guess returns a Hint object instance that tells:
 * - which characters in the guess are correct, and correctly located.
 * - which characters in the guess are in the secret word, but not at the location guessed
 * - which characters in the guess are not in the secret word
 *
 * If all characters in the guess are correctly located, the game is won ;)
 */
public class Wordle {


    private ArrayList<String> knownWords;
    private String secretWord;
    private Random rnd = new Random(); // a source of random numbers

    /**
     * Create a new Wordle game, loading allowable words from a file.
     * See comments on loadWords() for the file format.
     *
     * @param file    - the file from which to load 'known words'
     * @param length  - the minimum length word
     * @param minfreq - the minimum allowed frequency
     * @param maxfreq - the maximum allowed frequency
     */
    public Wordle(String file, int length, long minfreq, long maxfreq) throws IOException {
        // ~12K 5 letter words with min frequency of 100,000
        loadWords(file, length, minfreq, maxfreq);
    }

    /**
     * _Part 1: Implement this constructor._
     *
     * Load known words from the array that is supplied.  It's ok if the
     * array contains duplicates, but the constructor should verify
     * that all loaded strings are the same length and are lowercase.
     *
     * @param words - an array of words to load
     */

    public Wordle(String[] words) {
        // Adds words to knownWords that have the same length
        // as the first word and converts them to all lowercase
        knownWords = new ArrayList<>();
        String firstWord = words[0];
        int wordLen = firstWord.length();

        for (int i = 0; i < words.length; i++) {
            if (words[i].length() == wordLen){
                knownWords.add(words[i].toLowerCase());
            }
        }
    }

    public int numberOfKnownWords() {
        return knownWords.size();
    }

    /**
     * _Part 2: Implement this method._
     *
     * Load words from a file. Each line of the file contains a word followed by one or more spaces followed
     * by a number whose value is higher for words that are more 'frequently occurring' than others.
     * Loaded words should have a frequency value in the range [minfreq, maxfreq]. However, at times, these
     * limits should be ignored (see comments below). Note that the words in the file may be many lengths
     * (e.g., 3-10 characters). Only words of the specified length should be loaded.
     *
     * Hint: use a Scanner instance to read in the file and look for the next String or Long value.
     * the following pattern will be useful ;)
     * Scanner scan = new Scanner(new File(filenm));
     *
     * The line above creates a Scanner from a File with the filename stored in the variable filenm
     * Use the scanner's .hasNext() method to test if there are more lines to read.
     * Use the scanner's .next() method to grab the next String token (word).
     * The scanner's .nextLong() method will grab the next token as a number (use this to read the frequency).
     *
     * For each line, you will want to call scan.next() and scan.nextLong() to read the data
     * identifying a word and its relative frequency.
     *
     * Words of the specified length should be added into the knownWords list if their frequencies
     * are the in the range specified.
     *
     * Hint: somewhere around 10-20 lines is probably appropriate here unless you have a lot of comments
     *
     * @param filenm  - the file name to load from
     * @param length  - the length of words we want to load (e.g., 5 to load 5 character words)
     * @param minfreq - the minimum allowable frequency for a loaded word
     * @param maxfreq - the maximum allowable frequency for a loaded word; 0 indicates no maximum
     */

    public void loadWords(String filenm, int length, long minfreq, long maxfreq) throws IOException {
        // As long as the next line in document contains data, this will check for passed word length
        // and passed frequency parameters and add that to wordHolder
        int counter = 1;
        Scanner scan = new Scanner(new File(filenm));
        knownWords = new ArrayList<>();

        while (counter == 1){
            String wordHolder = scan.next();
            int wordHoldLen = wordHolder.length();
            long freqHolder = scan.nextLong();

            if ((wordHoldLen == length) && ((freqHolder <= maxfreq || maxfreq == 0) && freqHolder >= minfreq))
                knownWords.add(wordHolder);

            if (scan.hasNext() == false) counter = 0;
        }

        return;
    }

    /**
     * _Part 3: Implement this method._
     *
     * Obtain a list of known words. This method creates a new copy of the known words list.
     * Here, you simply need to copy the knownWords list and return that copy.
     *
     * @return a new copy of list of known words.
     */
    public ArrayList<String> getKnownWords() {
        // This will create a separate array containing a copy of
        // the contents of knownWords
        ArrayList<String> knownWordsCopy = new ArrayList<>();
        knownWordsCopy.addAll(knownWords);
        return knownWordsCopy;
    }

    /**
     * Prepare the game for playing by choosing a new secret word.
     */
    public void initGame() {
        Random r = new Random();
        secretWord = knownWords.get(r.nextInt(knownWords.size()));
    }

    /**
     * Supply a guess and get a hint!
     *
     * Note that this implementation DOES NOT require that the guess be selected
     * from the known words. Rather, this implementation allows one to guess arbitrary
     * characters, so long as the guess is the same length as the secret word.
     *
     * @param g - the guess (a string which is the same length as the secret word)
     * @return a hint indicating the letters guessed correctly/incorrectly
     * @throws IllegalArgumentException if the guess is not the same length as the secret word
     */
    public Hint guess(String g) {
        int length = secretWord.length();
        if (length != g.length()) {
            throw new IllegalArgumentException("Wrong length guess!");
        }
        return new Hint(g, secretWord);

    }

}

/**
 * NOTE:
 *
 * We're breaking one of our initial rules about Java.
 * The truth is...more than one class CAN live in a file.
 *
 * However, only one public class can live in a file.
 * The Hint class below is not public.
 *
 * Style-wise, Hint would probably be best as a public class
 * because the intent is for it to be used widely. However,
 * that makes submission to autolab much more complicated.
 * And as a result, I have opted to keep it in the same file
 * as the Wordle class itself.
 */
class Hint {
    // why private?
    // because we want the hint to be generated by the constructor and then
    // not messed with further. So, we restrict access to these variables.
    private String correctlyPlaced;
    private String incorrectlyPlaced;
    private String notInPuzzle;
    private String guess;

    /**
     * _Part 4: Implement this Constructor._
     *
     * Given a guess and the secret word, provide a hint.
     *
     * The Hint should follow these guidelines:
     * - the Hint should not store any knowledge of the secret word itself.
     * - correctlyPlaced should have a length equal to the length of the secret word
     * - incorrectlyPlaced should have a length equal to the length of the secret word
     * - notInPuzzle should have a length less than or equal to the length of the secret word/guess.
     *
     * A character in the guess that is both the correct letter, and in the correct location in the word
     * will appear in that location in the correctlyPlaced String. Characters in the guess that are not
     * correctly placed will appear as the char '-' in the correctlyPlaced String.
     *
     * Thus, for a guess "skate" and a secret word "score", the correctlyPlaced will be: "s---e"
     *
     * A character in the guess that is the correct letter, but not in the correct location in the word
     * will appear in the same location as the guess when placed in the incorrectedPlaced String.
     * Note that duplicate characters are a bit tricky, they must be examined for correctly placed characters
     * before incorrectly placed ones.
     *
     * Thus, for a guess "scoop" and a secret word "poofs", the correctlyPlaced String is: "--o--" and
     * incorrectlyPlaced String is: "s--op"
     * Note that the first 'o' is correctly placed, thus it must be the second 'o' that is incorrectly placed.
     * It is incorrect to say that the first 'o' is incorrectly placed, as it is incorrect to say that
     * both 'o's are incorrectly placed.
     *
     * Characters that are in the guess and are neither correctly placed nor incorrectly placed should be
     * added to the notInPuzzle String.  Thus, the length of the notInPuzzle String will never be greater
     * than the length of the secret word, or the guess.
     *
     * @param guess
     * @param secretWord
     */
    public Hint(String guess, String secretWord) {
        // Checks each individual character of the guessed word against
        // the secret word and displays the correct guess and position,
        // the correctly guessed by wrong position, and those that are
        // not in the secret word at all.

        /**
        * JUSTIFICATION:
         *
         * The Hint class needed a way to iterate through the guess
         * and separate what was correct and in the correct position from
         * what was incorrect, or not in the secret word at all.
         *
         * Character arrays were used to allow iteration over each
         * individual character of any word used. (i.e. guess, secretWord, etc..)
         *
         * During the correctlyPlaced check, the correct words and placement were
         * altered to special characters, so that the incorrectlyPlaced check would
         * ignore those.
         *
         * The same method was done with an alternative char[] of the guess and
         * secretLetter. These were passed the altered arrays from before, and altered
         * further in order to pass repeated letters that didn't fit, to notInPuzzle
         * */

        char[] secretLetter = new char[secretWord.length()];
        char[] correctlyGuessed = new char[secretWord.length()];
        char[] incorretlyGuessed = new char[secretWord.length()];
        char[] altGuess = guess.toCharArray();
        char[] altSecretLetter;
        String notIn = new String("");
        this.guess = guess;



        for (int i = 0; i < secretWord.length(); i++){
            secretLetter[i] = secretWord.charAt(i);
        }

        for (int i = 0; i < secretWord.length(); i++ ){
            if (guess.charAt(i) == secretLetter[i]){
                correctlyGuessed[i] = guess.charAt(i);
                secretLetter[i] = '?';
                altGuess[i] = '!';
            }else{
                correctlyGuessed[i] = '-';
            }
        }
        correctlyPlaced = new String(correctlyGuessed);

        altSecretLetter = secretLetter;


        for (int i = 0; i < secretWord.length(); i++){
            for (int j = 0; j < secretWord.length(); j++){
                if (altGuess[i] == altSecretLetter[j]){
                    incorretlyGuessed[i] = altGuess[i];
                    altGuess[i] = '!';
                    altSecretLetter[j] = '?';
                    break;
                }else{
                    incorretlyGuessed[i] = '-';
                }
            }
            if ((correctlyGuessed[i] == '-') && (incorretlyGuessed[i] == '-')){
                notIn += guess.charAt(i);
            }
        }
        incorrectlyPlaced = new String(incorretlyGuessed);
        notInPuzzle = notIn;



    }

    public boolean isWin() {
        // true iff the '-' isn't in the correctlyPlaced String...
        return (correctlyPlaced.indexOf('-') == -1);
    }

    /**
     * Display a hint on System.out
     *
     * Given a secret word: 'state', and a guess 'scope' display:
     *
     * ---- Hint (scope) ----
     * Correctly placed  : s---e
     * Incorrectly placed: -----
     * Not in the puzzle : [cop]
     *
     * Given a secret word: 'state', and a guess 'sttae' display:
     *
     * ---- Hint (scope) ----
     * Correctly placed  : st--e
     * Incorrectly placed: --ta-
     * Not in the puzzle : []
     */
    public void write() {
        System.out.println("---- Hint (" + guess + ") ----");
        System.out.println("Correctly placed  : " + correctlyPlaced);
        System.out.println("Incorrectly placed: " + incorrectlyPlaced);
        System.out.println("Not in the puzzle : [" + notInPuzzle + "]");
    }

    /**
     * Note that we can return a reference to the correctlyPlaced String
     * safely since String's aren't immutable.  Thus, someone that messes
     * with the result we return won't actually impact the String
     * referenced by the Hint itself...
     *
     * @return the correctly placed portion of the hint
     */
    public String getCorrectlyPlaced() {
        return correctlyPlaced;
    }

    /**
     *
     * @return the incorrectly placed portion of the hint
     */
    public String getIncorrectlyPlaced() {
        return incorrectlyPlaced;
    }

    /**
     *
     * @return the not-in-puzzle portion of the hint
     */
    public String getNotInPuzzle() {
        return notInPuzzle;
    }
}
