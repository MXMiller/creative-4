package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException, GuessAlreadyMadeException {
        String fileName = args[0];
        int wordLength = 2;
        if(Character.isDigit(args[1].charAt(0)) && Integer.parseInt(args[1]) >= 2){
            wordLength = Integer.parseInt(args[1]);
        }
        int guesses = 1;
        if(Character.isDigit(args[2].charAt(0)) && Integer.parseInt(args[1]) >= 1){
            guesses = Integer.parseInt(args[2]);
        }

        File f = new File(fileName);

        EvilHangmanGame game = new EvilHangmanGame();

        game.startGame(f, wordLength);

        Scanner sc = new Scanner(System.in);

        StringBuilder displayWord = new StringBuilder();
        for(int i = 0; i < wordLength; i++){
            displayWord.append('-');
        }

        for(int i = 0; i < guesses; guesses--){

            System.out.println("You have " + guesses + " guesses left");

            StringBuilder usedChars = new StringBuilder();
            for(Character s : game.getGuessedLetters()){
                usedChars.append(" " + s);
            }

            System.out.println("Used letters:" + usedChars);

            System.out.println("Word: " + displayWord);

            System.out.println("Enter guess: ");

            char guess = sc.next().charAt(0);
            guess = Character.toLowerCase(guess);

            if(Character.isAlphabetic(guess)){
                Set<String> set;
                try{
                    set = game.makeGuess(guess);
                } catch (GuessAlreadyMadeException e){
                    System.out.println("You already guessed that\n");
                    guesses++;
                    continue;
                }

                //count number of guesses char in dictionary
                int numInWord = 0;
                String word = set.iterator().next();

                for(int j = 0; j < word.length(); j++){
                    if(word.charAt(j) == guess){
                        numInWord++;
                    }
                }

                if(numInWord == 0){
                    if(guesses <= 1){

                        String theWord = "";

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
                    for(int j = 0; j < word.length(); j++){
                        if(word.charAt(j) == guess){
                            displayWord.replace(j, j + 1, Character.toString(guess));
                        }
                    }

                    int blank = 0;
                    for(int j = 0; j < displayWord.length(); j++){
                        if(displayWord.charAt(j) == '-'){
                            blank++;
                        }
                    }

                    if(blank == 0){
                        System.out.println("You win!");
                        System.out.println("The word was: " + word);
                        guesses = 0;
                    } else {
                        System.out.println("Yes, there is " + numInWord + " " + guess + "\n");
                        guesses++;
                    }
                }
            } else {
                System.out.println("Invalid input\n");
                guesses++;
            }
        }
    }

}
