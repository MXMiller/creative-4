package spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector{

    Trie dictionary;

    public SpellCorrector() {
        dictionary = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner sc = new Scanner(new File(dictionaryFileName));

        while(sc.hasNext()){
            String curr = new String(sc.next());
            dictionary.add(curr);
        }

        //System.out.println(dictionary.toString());
    }

    @Override
    public String suggestSimilarWord(String inputWord) {

        INode n = dictionary.find(inputWord);

        if(n != null){
            return inputWord.toLowerCase();
        } else {

            String best = "";
            INode bestNode = new Node();

            java.util.List<String> edits = genEdits(inputWord);

            for(int i = 0; i < edits.size(); i++){
                INode currNode = dictionary.find(edits.get(i));
                if(currNode != null){
                    if(currNode.getValue() > bestNode.getValue()){
                        bestNode = currNode;
                        best = edits.get(i);
                    } else if(currNode.getValue() == bestNode.getValue()){
                        //if the new node comes before the old best
                        if(alphabatise(edits.get(i), best)){
                            bestNode = currNode;
                            best = edits.get(i);
                        }
                    }
                }
            }

            if(best != ""){
                return best;
            }

            //gen 2nd edits from first edits
            for(int i = 0; i < edits.size(); i++){
                java.util.List<String> edits2 = genEdits(edits.get(i));
                for(int j = 0; j < edits2.size(); j++){
                    INode currNode = dictionary.find(edits2.get(j));
                    if(currNode != null){
                        if(currNode.getValue() > bestNode.getValue()){
                            bestNode = currNode;
                            best = edits2.get(j);
                        } else if(currNode.getValue() == bestNode.getValue()){
                            //if the new node comes before the old best
                            if(alphabatise(edits2.get(j), best)){
                                bestNode = currNode;
                                best = edits2.get(j);
                            }
                        }
                    }
                }
            }

            if(best != ""){
                return best;
            } else {
                return null;
            }
        }
    }

    public java.util.List<String> genEdits(String word){

        java.util.List<String> newWords = new ArrayList<>();

        //deletions
        for(int i = 0; i < word.length(); i++){
            StringBuilder newWord = new StringBuilder(word);
            newWord.deleteCharAt(i);

            newWords.add(newWord.toString());
        }

        //swapping
        for(int i = 0; i < word.length() - 1; i++){
            StringBuilder newWord = new StringBuilder(word);
            StringBuilder oldWord = new StringBuilder(word);

            newWord.setCharAt(i, oldWord.charAt(i + 1));
            newWord.setCharAt(i + 1, oldWord.charAt(i));

            newWords.add(newWord.toString());
        }

        //aleterations
        for(int i = 0; i < word.length(); i++){
            for(int j = 0; j < 26; j++){
                StringBuilder newWord = new StringBuilder(word);
                newWord.setCharAt(i, (char) ('a' + j));

                newWords.add(newWord.toString());
            }
        }

        //insertions
        for(int i = 0; i < word.length() + 1; i++){
            for(int j = 0; j < 26; j++){
                StringBuilder newWord = new StringBuilder(word);
                newWord.insert(i, (char) ('a' + j));

                newWords.add(newWord.toString());
            }
        }

        return newWords;
    }

    public boolean alphabatise(String s1, String s2){
        if(s1.length() > 0 && s2.length() > 0){
            if(s1.charAt(0) < s2.charAt(0)){
                return true;
            } else if(s1.charAt(0) > s2.charAt(0)){
                return false;
            } else {
                return alphabatise(s1.substring(1), s2.substring(1));
            }
        } else {
            if(s1.length() > s2.length()){
                return false;
            } else {
                return true;
            }
        }
    }
}
