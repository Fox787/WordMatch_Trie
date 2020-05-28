import java.util.ArrayList;

/*
 * Class Name:    TrieNode
 *
 * Author:        Connor Parker
 * Last Modified: Tuesday, May 19th:
 *
 * A Class representation of Trie nodes.
 * the individual nodes of our system
 */


public class TrieNode {

    // non-empty when node is a leaf node
    private String key= null;
    private TrieNode[] character = null;

    //character for stringbuilder context during recusive calls
    private char c;
    private int count =0;
    private ArrayList<String> matches = new ArrayList<>(0);

    TrieNode(){
        // Trie supports lowercase English characters (a - z)
        // so character size is 26
        character = new TrieNode[26];
    }


    public TrieNode[] getCharacter() {
        return character;
    }

    public void setCharacter(TrieNode[] character) {
        this.character = character;
    }

    public int getCount(){
        return this.count;
    }

    public String getKey() {
        return key;
    }

    //Has Impacts count for keeping track of duplicate and word occurances as required
    public void setKey(String key) {
        if (this.key != null){
            if (this.key.equals(key)){
                count +=1;
            }
        }
        else{
            this.key = key;
            count = 1;
        }

    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public ArrayList<String> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<String> matches) {
        this.matches = matches;
    }

    public String matchToString() {
        String out = "";
        for (String matchOutput : matches
        ) {
            if (out.equals("")) {
                out = matchOutput;
            } else {
                out = out + ", " + matchOutput;
            }

        }
        return out;
    }
}
