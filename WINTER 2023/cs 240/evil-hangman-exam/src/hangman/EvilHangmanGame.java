package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{

    Set<String> currDictionary;

    SortedSet<Character> guesses;

    public EvilHangmanGame() {
        currDictionary = new TreeSet<>();
        guesses = new TreeSet<>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner sc = new Scanner(dictionary);
        while(sc.hasNext()){
            String s = sc.next();
            if(s.length() == wordLength){
                currDictionary.add(s);
            }
        }

        if(currDictionary.size() == 0){
            throw new EmptyDictionaryException();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        guess = Character.toLowerCase(guess);

        if(guesses.contains(guess)){
            throw new GuessAlreadyMadeException();
        }

        guesses.add(guess);

        //partitions things
        Map<String, Set<String>> partition = new TreeMap<>();
        for(String s : currDictionary){
            String key = getKey(s, guess);

            if(partition.get(key) == null){
                Set<String> words = new TreeSet<>();
                words.add(s);
                partition.put(key, words);
            } else {
                Set<String> words = partition.get(key);
                words.add(s);
                partition.replace(key, words);
            }
        }

        Set<String> largestWords = new TreeSet<>();

        //find best thing
        for(String s : currDictionary){
            String key = getKey(s, guess);
            Set<String> someWords = partition.get(key);

            if(someWords.size() > largestWords.size()){
                largestWords = someWords;
            } else if(someWords.size() == largestWords.size()){
                //choose empty one or longer one
                int num1 = 0;
                int num2 = 0;

                String s1 = getKey(someWords.iterator().next(), guess);
                String s2 = getKey(largestWords.iterator().next(), guess);

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

                if(num1 > num2 || s1.length() > s2.length()){
                    largestWords = someWords;
                } else if(num1 == num2){
                    //in neither choose one with least amount of guess

                    num1 = 0;
                    num2 = 0;

                    s1 = getKey(someWords.iterator().next(), guess);
                    s2 = getKey(largestWords.iterator().next(), guess);

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

                    if(num1 > num2) {
                        largestWords = someWords;
                    } else if(num1 == num2){
                        //choose one with largest guess index
                        int posNum1 = 0;
                        int posNum2 = 0;

                        s1 = getKey(someWords.iterator().next(), guess);
                        s2 = getKey(largestWords.iterator().next(), guess);

                        for(int i = 0; i < s1.length(); i++){
                            if(s1.charAt(i) == guess){
                                posNum1 += i;
                            }
                        }

                        for(int i = 0; i < s2.length(); i++){
                            if(s2.charAt(i) == guess){
                                posNum2 += i;
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
        return guesses;
    }
}
