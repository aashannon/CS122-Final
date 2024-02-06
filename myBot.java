import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class myBot implements WordlePlayer{

    ArrayList<String> givenWords;

    int numGuesses;

    String getFirstGuess;

    String eduGuessWord;

    public myBot(){}

    public int tieBreak (){
        // Creates and returns a random number between 0 and the size of the passed ArrayList

        Random rand = new Random();
        int upperLimit = givenWords.size();
        int randNum = rand.nextInt(upperLimit);

        return randNum;
    }

    @Override
    public void beginGame(Wordle game) {
        // Grabs ArrayList and sets guesses to 0

        givenWords = game.getKnownWords();
        numGuesses = 0;
    }

    @Override
    public boolean hasNextGuess() {
        // Checks if there is another String in the List

        return (givenWords.size() != 0);
    }

    @Override
    public String nextGuess() {
        // Returns String if List is 1
        // Random String returned if first guess
        // Calls eduGuess for second guess, otherwise returns random guess again

        if (givenWords.size() == 1) {
            return givenWords.get(0);

        }else if (numGuesses == 0) {
            numGuesses++;
            return initGuess();

        }else if(numGuesses == 1){
            numGuesses++;
            return eduGuess();

        }else{
            numGuesses++;
            return givenWords.get(tieBreak());
        }
    }

    public String initGuess() {
        // Returns a random String from ArrayList

        String firstGuess = givenWords.get(tieBreak());
        getFirstGuess = firstGuess;
        return firstGuess;

    }

    public String eduGuess() {
        // Returns a String that does not contain any of the characters from the first guess

        for (int i = 0; i < givenWords.size(); i++){
            for (int j = 0; j < getFirstGuess.length(); j++ ){
                if (givenWords.get(i).contains("" + getFirstGuess.charAt(j))){
                    continue;
                }else{
                    eduGuessWord = givenWords.get(i);
                }
            }
        }
        return eduGuessWord;
    }

    @Override
    public void tell(Hint h) {
        // Checks if List against characters found in NotInPuzzle, removes if matches
        // Checks if character in IncorrectlyPlaced matches the character in the same place, removes if matches
        // Continues if there is a dash in the character at index for CorrectlyPlaced
        // Checks if character at index matches CorrectlyPlaced, removes if it doesn't match

        String hintCP = h.getCorrectlyPlaced();
        String hintIP = h.getIncorrectlyPlaced();
        String hintNIP = h.getNotInPuzzle();

        if (!h.isWin()) {
            givenWords.remove(h);
        }

        for (int i = 0; i < givenWords.size(); i++){
            for (int j = 0; j < hintCP.length(); j++) {
                if (hintNIP.contains(""+givenWords.get(i).charAt(j))) {
                    if (!hintCP.contains("" + givenWords.get(i).charAt(j)) && !hintIP.contains("" + givenWords.get(i).charAt(j))) {
                        givenWords.remove(givenWords.get(i));
                        i--;
                        break;
                    }

                } else if (givenWords.get(i).charAt(j) == hintIP.charAt(j)) {
                    givenWords.remove(givenWords.get(i));
                    i--;
                    break;

                }else if ((hintCP.charAt(j) == '-')) {
                    continue;

                } else if (givenWords.get(i).charAt(j) != hintCP.charAt(j) ){
                    givenWords.remove(givenWords.get(i));
                    i--;
                    break;

                }
            }

        }
    }

//    public static void main(String[] args) throws IOException {
////        String[] words = {"colby", "bacon"};
//        myWordle game = new myWordle("norvig200.txt", 5, 100000, 0);
//        game.initGame();
//
//        myBot bot = new myBot();
//        bot.beginGame((Wordle) game);
//        Hint h = null;
//        int guess = 0;
//        int maxGuesses = 100;
//        while( bot.hasNextGuess() && guess < maxGuesses ) {
//            guess++;
//            h = game.guess(bot.nextGuess());
//            bot.tell(h);
//            if (h.isWin()) {
//                break;
//            }
//        }
//        System.out.println("Game over: bot " + (h.isWin()?"won":"lost") + " with " + guess + " guesses");
//    }
}
