package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{

    Set<String> currDictionary;

    SortedSet<Character> guessedChars;

    public EvilHangmanGame() {
        currDictionary = new TreeSet<String>();
        guessedChars = new TreeSet<Character>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner sc = new Scanner(dictionary);

        while (sc.hasNext()) {
            StringBuilder curr = new StringBuilder();
            curr.append(sc.next());
            if(curr.length() == wordLength){
                currDictionary.add(curr.toString());
            }
        }

        if(currDictionary.size() == 0){
            throw new EmptyDictionaryException();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        guess = Character.toLowerCase(guess);

        if(getGuessedLetters().contains(guess)) {
            throw new GuessAlreadyMadeException();
        }

        guessedChars.add(guess);

        Map<String, Set<String>> partition = new TreeMap<>();

        Set<String> largestWords = new TreeSet<String>();
        Set<String> someWords;

        //partition
        for(String word : currDictionary){
            String key = getKey(word, guess);

            if(partition.get(key) == null){
                someWords = new TreeSet<String>();
                someWords.add(word);
                partition.put(key, someWords);
            } else {
                someWords = partition.get(key);
                someWords.add(word);
                partition.replace(key, someWords);
            }
        }

        //find best partition
        for(String word : currDictionary){

            String key = getKey(word, guess);
            someWords = partition.get(key);

            //see if someWords is better than largestWords
            if(someWords.size() > largestWords.size()){
                largestWords = someWords;
            } else if(someWords.size() == largestWords.size()){

                //if the letter doesnt appear
                int num1 = 0;
                int num2 = 0;

                String s1 = getKey(someWords.iterator().next(), guess);
                String s2 = getKey(largestWords.iterator().next(), guess);

                if(!s1.equals(s2)){
                    for(int i = 0; i < s1.length(); i++){
                        if(s1.charAt(i) != guess){
                            num1++;
                        }
                    }

                    for(int i = 0; i < s2.length(); i++){
                        if(s2.charAt(i) != guess){
                            num2++;
                        }
                    }
                }

                if(num1 > num2 || s1.length() > s2.length()){
                    largestWords = someWords;
                } else if(num1 == num2){
                    //the one with the lowest number of the letter
                    num1 = 0;
                    num2 = 0;

                    s1 = getKey(someWords.iterator().next(), guess);
                    s2 = getKey(largestWords.iterator().next(), guess);

                    for(int k = 0; k < s1.length(); k++){
                        if(s1.charAt(k) != guess){
                            num1++;
                        }
                    }
                    for(int k = 0; k < s2.length(); k++){
                        if(s2.charAt(k) != guess){
                            num2++;
                        }
                    }

                    if(num1 > num2){
                        largestWords = someWords;
                    } else if(num1 == num2){

                        //the letters are in the rightmost position
                        int posNum1 = 0;
                        int posNum2 = 0;

                        s1 = getKey(someWords.iterator().next(), guess);
                        s2 = getKey(largestWords.iterator().next(), guess);


                        for(int k = 0; k < s1.length(); k++){
                            if(s1.charAt(k) == guess){
                                posNum1 += k;
                            }
                        }
                        for(int k = 0; k < s2.length(); k++){
                            if(s2.charAt(k) == guess){
                                posNum2 += k;
                            }
                        }

                        if(posNum1 > posNum2){
                            largestWords = someWords;
                        }
                    }
                }
            }
        }

        currDictionary = largestWords;

        return currDictionary;
    }

    public String getKey(String word, char guess){

        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) != guess){
                word = word.replace(word.charAt(i), '_');
            }
        }

        return word;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedChars;
    }

    public Set<String> getCurrDictionary() {
        return currDictionary;
    }
}
