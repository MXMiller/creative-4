package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException {
        //get user input
        String dictionaryName = args[0];
        int wordLength = 2;
        if(Character.isDigit(args[1].charAt(0)) && Integer.parseInt(args[1]) >= 2){
            wordLength = Integer.parseInt(args[1]);
        }
        int guesses = 1;
        if(Character.isDigit(args[2].charAt(0)) && Integer.parseInt(args[2]) >= 1){
            guesses = Integer.parseInt(args[2]);
        }

        //set up the evil hangman game

        EvilHangmanGame game = new EvilHangmanGame();

        File fileName = new java.io.File(dictionaryName);

        game.startGame(fileName, wordLength);

        Scanner sc = new Scanner(System.in);

        StringBuilder displayWord = new StringBuilder();
        for(int j = 0; j < wordLength; j++){
            displayWord.append("-");
        }

        //evil hangman gameplay loop
        for (int i = 0; i < guesses; guesses--) {
            System.out.println("You have " + (guesses - i) + " guesses left");

            //print guessedChars
            StringBuilder usedChars = new StringBuilder();

            if(game.getGuessedLetters().size() > 0){
                for(Character c : game.getGuessedLetters()){
                    usedChars.append(" ").append(c);
                }
            }

            System.out.println("Used letters:" + usedChars);

            //print the display word with correct guesses in the right places. Use "-" for black spaces.
            System.out.println("Word: " + displayWord);

            //prompt user input
            System.out.println("Enter guess: ");

            //process user input
            char guess = sc.next().charAt(0);
            guess = Character.toLowerCase(guess);
            if (!Character.isAlphabetic(guess)) {
                System.out.println("Invalid input. \n");
                guesses++;
            } else {
                Set<String> set;
                try {
                    //change "set" to the largest partition of words
                    set = game.makeGuess(guess);
                } catch (GuessAlreadyMadeException e){
                    System.out.println("Guess already made. ");
                    guesses++;
                    continue;
                }

                //count the number of the guessed letter in the chosen word
                int numInSet = 0;
                for(int j = 0; j < set.iterator().next().length(); j++){
                    if(set.iterator().next().charAt(j) == guess){
                        numInSet++;
                    }
                }

                String theWord = set.iterator().next();

                if(numInSet == 0){

                    //the guessed letter isn't in the word

                    if(guesses <= 1){

                        //user ran out of guesses and lost the game

                        //pick a random word from "set"
                        int j = new Random().nextInt(set.size());
                        int k = 0;
                        for(String s : set){
                            if(k == j){
                                theWord = s;
                            }
                            k++;
                        }

                        System.out.println("You lose!");
                        System.out.println("The word was: " + theWord);
                    } else {
                        System.out.println("Sorry, there are no " + guess + "'s\n");
                    }
                } else {

                    //the guessed letter is in the word

                    //update the display word
                    for(int j = 0; j < game.getCurrDictionary().iterator().next().length(); j++){
                        if(game.getCurrDictionary().iterator().next().charAt(j) == guess){
                            displayWord.replace(j, j + 1, Character.toString(guess));
                        }
                    }

                    if(displayWord.toString().endsWith(theWord)){
                        //user guessed the word
                        System.out.println("You win!");
                        System.out.println("The word was: " + theWord);
                        guesses = 0;
                    } else {
                        //the guessed latter is in the word
                        System.out.println("Yes, there is " + numInSet + " " + guess + "\n");
                        guesses++;
                    }
                }
            }
        }
    }
}
