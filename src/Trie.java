import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/*
 * Class Name:    Trie
 *
 * Author:        Connor Parker
 * Last Modified: Tuesday, May 19th:
 *
 * A Class representation of a Trie.
 * Including helper functions specific to out project.
 *
*/
class Trie {
    private TrieNode root;
    //Global change to Wildcard
    public static final char WILDCARD = '?';
    public ArrayList match = new ArrayList(0);

    // Constructor
    Trie() {
        root = new TrieNode();
    }

    //Insertion starts from root node
    public void insert(String str) {
        // start from root node
        TrieNode curr = root;

        for (int i = 0; i < str.length(); i++) {
            TrieNode[] temp = curr.getCharacter();
            // create a new node if path doesn't exists
            if (temp[str.charAt(i) - 'a'] == null) {
                temp[str.charAt(i) - 'a'] = new TrieNode();
                // maintains information of invidual character at node
                temp[str.charAt(i) - 'a'].setC(str.charAt(i));
            }
            // go to next node
            curr = temp[str.charAt(i) - 'a'];
        }

        // maintains information of full word at leaf nodes
        // any node with a Key Set, is a Complete word.
        curr.setKey(str);

    }

    //A Preorder traversal of Trie tree starting from given node
    //Boolean out represents if full output information is needed, or just keys.
    //This overloaded form is for recursive calls
    public ArrayList<String> preorder(TrieNode curr, Boolean out) {
        ArrayList<String> res = new ArrayList<>(0);
        // return false if Trie is empty
        if (curr == null) {
            return res;
        }
        //26 for total number of letters as needs to iterate over all of them looking for potential nodes.
        for (int i = 0; i < 26; i++) {
            if (curr.getCharacter()[i] != null) {
                // if leaf node pulls information
                if (curr.getCharacter()[i].getKey() != null) {
                    // If flagged for output, full information is taken word + count + match.
                    //else just word is taken
                    if (out) {
                        res.add(curr.getCharacter()[i].getKey() + " " + curr.getCharacter()[i].getCount() + " [" + curr.getCharacter()[i].matchToString() + "]");
                    } else {
                        res.add(curr.getCharacter()[i].getKey());
                    }
                }

                //recersivly calling to investigate all node lengths
                ArrayList<String> temp = preorder(curr.getCharacter()[i], out);
                if (temp.size() > 0) {
                    res.addAll(temp);
                }
            }
        }
        return res;
    }

    /*
    Overloaded function for preorder same description as above
    For when no node is provided and starting at root
    any recursive call uses above method
    */
    private ArrayList<String> preorder(Boolean out) {
        ArrayList<String> result = new ArrayList<String>(0);

        TrieNode curr = root;
        // return false if Trie is empty
        if (curr == null) {
            return null;
        }

        for (int i = 0; i < 26; i++) {
            if (curr.getCharacter()[i] != null) {
                if (curr.getCharacter()[i].getKey() != null) {
                    if (out) {
                        result.add(curr.getCharacter()[i].getKey() + " " + curr.getCharacter()[i].getCount() + " [" + curr.getCharacter()[i].matchToString() + "]");
                    } else {
                        result.add(curr.getCharacter()[i].getKey());
                    }
                }
                ArrayList<String> temp = preorder(curr.getCharacter()[i], out);
                if (temp.size() > 0) {
                    result.addAll(temp);
                }
            }
        }
        return result;
    }

    public TrieNode getRoot() {
        return root;
    }

    /*
    Method called to assign matches to all words within the Trie
    works by getting a list of all words via preorder
    individualy replacing all characters of a word with ?
    to find silimar words based on our definition
     */

    public void setMatch() {
        //not for output
        boolean out = false;
        //list of all words in trie
        ArrayList<String> Words = preorder(out);
        ArrayList<String> res;
        if (Words == null) {
            System.out.println("Error Matching");
        } else {
            for (int i = 0; i < Words.size(); i++) {
                String currentWord = Words.get(i);
                ArrayList<String> match = new ArrayList<>(0);


                //Wildcard substitution on string
                //cat = ?at, c?t, ca?
                for (int j = 0; j < currentWord.length(); j++) {
                    //Issues when not using Stringbuilder.
                    //string.Replace would replace all instances of same character
                    //AAA = ???, which by wildcard logic would match any 3 letter word
                    StringBuilder word = new StringBuilder(currentWord);
                    word.insert(j + 1, '?');
                    word.deleteCharAt(j);

                    //Call function to recursivly look for words in trie using wildcard.
                    res = wildcardMatches(word.toString());

                    //for list of results
                    for (int k = 0; k < res.size(); k++) {
                        //just incase checking wordlength
                        if (res.get(k).length() == currentWord.length()) {
                            //word can't match itself
                            if (!res.get(k).equals(currentWord)) {
                                match.add(res.get(k));
                            }
                        }
                    }
                }
                //locate node of word searched and sets all matches
                searchNode(currentWord).setMatches(match);

            }

        }
    }

    // Returns if the word is in the trie.
    public boolean search(String str) {
        TrieNode curr = searchNode(str);
        if (curr == null) {
            return false;
        } else {
            if (curr.getKey() != null)
                return true;
        }

        return false;
    }

    // Returns if there is any word in the trie
    // With that given prefix
    public boolean startsWith(String start) {
        TrieNode curr = searchNode(start);
        if (curr == null) {
            return false;
        } else {
            return true;
        }
    }


    // Searches Trie for Given node for string
    public TrieNode searchNode(String str) {
        //Starts at root
        TrieNode curr = root;

        //follows nodes for a word character by character
        for (int i = 0; i < str.length(); i++) {
            //if node != null it exists
            if (curr.getCharacter()[str.charAt(i) - 'a'] != null) {
                curr = curr.getCharacter()[str.charAt(i) - 'a'];
            } else break;
        }
        if (curr == root) {
            return null;
        }
        return curr;
    }


    //Helper class for wildcard matching.
    public ArrayList<String> wildcardMatches(String str) {
        ArrayList<String> wildcardMatches = new ArrayList<>();
        /*
        Calls recursive function to look for words
        Takes String to match,
        Stringbuilder for recusive comparision,
        Node to search from - always begins with root
        initial length of 0, holds where we're looking with stringbuilder
        Arraylist to maintain information
        */

        wildcardTraverse(str, new StringBuilder(), root, 0, wildcardMatches);
        //returns list of results
        return wildcardMatches;
    }

    //Method to recursivly look for words in trie using wildcard.
    private void wildcardTraverse(String pattern, StringBuilder prefix, TrieNode node, int len,
                                  ArrayList<String> wildcardMatches) {
        //If node is null it doesn't exist in trie
        if (node == null) {
            return;
        }
        // if lengt == patten length and not on a null node, add to list
        if (len == pattern.length()) {
            if (node.getKey() != null) {
                wildcardMatches.add(prefix.toString());
            }
            return;
        }

        //recursive steps based on finding Wildcard '?'
        if (pattern.charAt(len) == WILDCARD) {
            //Checks all possible connecting node inplace of a ?
            for (int i = 0; i < 26; i++) {
                //If node were looking at is null it doesn't exist in trie
                if (node.getCharacter()[i] != null) {
                    //append character looking at in stringbuilder
                    prefix.append(node.getCharacter()[i].getC());
                    //recursive call with context on stringbuilder and node we're seaching
                    wildcardTraverse(pattern, prefix, node.getCharacter()[i], len + 1, wildcardMatches);
                    //clear the character added to stringbuilder
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
        } else {
            //if wildcard isn't found, proceds traversing down string given
            prefix.append(pattern.charAt(len));
            wildcardTraverse(pattern, prefix, node.getCharacter()[pattern.charAt(len) - 'a'], len + 1, wildcardMatches);
            //removes string from builder once searched
            prefix.deleteCharAt(prefix.length() - 1);
        }

    }

    // s is String of only letters trying to match before any wildcards occur
    // str is full pattern to match
    public void wordMatch(String s, String str) {
        //checks that there is a word in tree starting with letters we have
        if (startsWith(s)) {
            //preorder search based on node were working with
            ArrayList<String> words = preorder(searchNode(s), false);

            //if words are found matching pattern calls patten match
            for (int i = 0; i < words.size(); i++) {
                //boolean to look for if it's a match to pattern
                if (patenMatch(words.get(i), str, words.get(i).length(), str.length())) {
                    // find node to work with
                    TrieNode temp = searchNode(words.get(i));
                    // add node information with full output information
                    match.add(temp.getKey() + " " + temp.getCount() + temp.getMatches() );
                }
            }
        } else {
            //Incase where no Words in Trie match Patten in in2.txt
            System.out.println("No Words match Inputed Pattern");
        }
    }

    //Boolean method for finding wildcard patterns
    private static boolean patenMatch(String wordGiven, String patternGiven,
                                      int wordLength, int patternLengrh) {
        // lookup table for storing results
        boolean[][] check = new boolean[wordLength + 1][patternLengrh + 1];

        // initailze lookup table to false
        for (int i = 0; i < wordLength + 1; i++) {
            for (int j = 0, len = check[i].length; j < len; j++)
                check[i][j] = false;
        }
        // empty pattern can match with empty string
        check[0][0] = true;

        // Only '*'can match with empty string
        for (int j = 1; j <= patternLengrh; j++) {
            if (patternGiven.charAt(j - 1) == '*') {
                check[0][j] = check[0][j - 1];
            }
        }

        // fill the table in bottom-up fashion
        for (int i = 1; i <= wordLength; i++) {
            for (int j = 1; j <= patternLengrh; j++) {
                // Two cases if we see a '*'
                // a) We ignore '*'' character and move
                //    to next  character in the pattern,
                //     i.e., '*' indicates an empty sequence.
                // b) '*' character matches with ith
                //     character in input

                if (patternGiven.charAt(j - 1) == '*') {
                    //Two Cases on '*'
                    //Ignore it and treat it as blankspace or it matches input
                    check[i][j] = check[i][j - 1] || check[i - 1][j];
                }
                //Two cases on '?', The character is ?, or characters of both word and pattern match
                else if (patternGiven.charAt(j - 1) == '?' || wordGiven.charAt(i - 1) == patternGiven.charAt(j - 1)) {
                    check[i][j] = check[i - 1][j - 1];
                }
                // If characters don't match
                else check[i][j] = false;
            }
        }
        //
        return check[wordLength][patternLengrh];
    }

//Helper method for Output 1 outputs all words and all information to out1.txt
    public void output1(String out1) {
        ArrayList<String> data = new ArrayList<>(0);
        File output = new File(out1);
        boolean out = true;
        try {
            //If it doesn't exist create it
            if (!output.exists()) {
                output.createNewFile();

            } else {
                //Other wise Delete and create.
                output.delete();
                output.createNewFile();
            }

            FileWriter writer = new FileWriter(output);
            data = preorder(out);
            for (int i = 0; i < data.size(); i++) {
                writer.write(data.get(i) + "\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error with " + output.getName());
        }
        System.out.println("Output 1 Successful");

    }

    //output helper 2 method
    public void output2(String out2) {
        File output2 = new File(out2);
        try {
            //If it doesn't exist create it
            if (!output2.exists()) {
                output2.createNewFile();

            } else {
                //Other wise Delete and create.
                output2.delete();
                output2.createNewFile();
            }

            FileWriter writer = new FileWriter(output2);
            for (int i = 0; i < match.size(); i++) {
                //toOutput returns string in Word + count + matches format as required
                writer.write(match.get(i) + "\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error with output");
        }
        System.out.println("Output 2 Successful");
    }


}