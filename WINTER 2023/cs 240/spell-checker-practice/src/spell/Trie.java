package spell;

import java.util.Objects;

public class Trie implements ITrie{

    int wordCount;
    int nodecount;
    Node root;

    public Trie() {
        wordCount = 0;
        nodecount = 1;
        root = new Node();
    }

    @Override
    public void add(String word) {

        int wordNum;

        if(find(word) == null){
            wordNum = 0;
        } else {
            find(word).incrementValue();
            wordNum = find(word).getValue();
        }

        if(wordNum == 0){
            addHelper(word, root, 0);
        }
    }

    public void addHelper(String word, INode n, int i){
        if(i < word.length()){
            char c = word.charAt(i);
            if(n.getChildren()[c - 'a'] == null){
                n.getChildren()[c - 'a'] = new Node();
                nodecount++;
            }
            addHelper(word, n.getChildren()[c - 'a'], i + 1);
        } else {
            n.incrementValue();
            if(n.getValue() == 1){
                wordCount++;
            }
            //System.out.println("added " + word + " to dictionary");
        }
    }

    @Override
    public INode find(String word) {
        return findHelper(word, root, 0);
    }

    public INode findHelper(String word, INode n, int i){
        if(n != null){
            if(i < word.length()){
                char c = word.charAt(i);
                while(c < 97){
                    c += 32;
                }
                if(n.getChildren()[c - 'a'] == null){
                    return null;
                }
                return findHelper(word, n.getChildren()[c - 'a'], i + 1);
            }

            if(i == word.length() && n.getValue() >= 1){
                //n.incrementValue();
                //System.out.println(word);
                return n;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodecount;
    }

    @Override
    public String toString() {
        StringBuilder curr = new StringBuilder();
        StringBuilder output = new StringBuilder();

        return toStringHelper(root, curr, output).toString();
    }

    public StringBuilder toStringHelper(INode n, StringBuilder curr, StringBuilder output){

        if(n.getValue() > 0){
            output.append(curr);
            output.append('\n');
        }

        for(int i = 0; i < 26; i++){
            INode c = n.getChildren()[i];
            if(c != null){
                StringBuilder s = new StringBuilder(curr);
                curr.append((char)(i + 'a'));
                toStringHelper(c, curr, output);
                curr = s;
            }
        }

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trie ot = (Trie)o;

        if(this.getWordCount() != ot.getWordCount()){
            return false;
        }

        return equalsHelper(this.root, ot.root);
    }

    public boolean equalsHelper(INode n1, INode n2){
        if(n1.getValue() == n2.getValue()){

            boolean b = false;

            for(int i = 0; i < 26; i++){
                if(n1.getChildren()[i] != null && n2.getChildren()[i] == null){
                    return false;
                } else if(n1.getChildren()[i] == null && n2.getChildren()[i] != null){
                    return false;
                }

                if(n1.getChildren()[i] != null && n2.getChildren()[i] != null){

                    if(n1.getValue() == n2.getValue()){
                        return equalsHelper(n1.getChildren()[i], n2.getChildren()[i]);
                    } else {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {

        int num = 0;
        int nulls = 0;

        for(int i = 0; i < root.getChildren().length; i++){
            if(root.getChildren()[i] == null){
                nulls += i;
            } else {
                num += root.getChildren()[i].getValue() + i;
            }
        }

        return 31 * wordCount + (31 * nodecount * num + (wordCount * nulls));
    }
}