import java.io.*;
import java.util.ArrayList;
/*
 * Class Name:    WordMatch
 *
 * Author:        Connor Parker
 * Last Modified: Tuesday, May 19th:
 *
 * Class Description: The main class for Assignment WordMatcher
 * Uses a Trie system to handle large sets of words
 *
 * Args[] = in1.txt out1.txt in2.txt out2.txt
 * in1 is a list of 2+ word files
 * in2 is a wildcard pattern to match, ? single replace * any
 * out 1 is output of all words of the trie including Word + Count + [ + Matches]+
 * out 2 is output of all words that satisfy the patten provided with same format
 *
 * by our definition a Match is any word that is within 1 character similarity of same length
 * e.g. Cat, Bat, Car, Cab,
 */

public class WordMatch {
    public static void main(String[] args){
        //input from arguments.
        String in1 = args[0];
        String out1 = args[1];
        String in2 = args[2];
        String out2 = args[3];


        Trie T = new Trie();
        ArrayList<String> files = new ArrayList<>();

        //load in list of files
        File file = new File(in1);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                files.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //iterate over files pulling words by our definition
        for (int i = 0; i < files.size(); i++) {
            try {
                File inputFile = new File(files.get(i));

                if (!inputFile.exists()) {
                    /// Doesn't Exist
                    System.out.println("File doesn't exist");
                } else {
                    //Check empty
                    if (inputFile.length() == 0) {
                        //file empty
                        System.out.println("File is empty");
                    } else {
                        BufferedReader br = new BufferedReader(new FileReader(inputFile));
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] a = line.split("([0-9])|([^\\w])");
                            //Any Number value 0-9
                            // [^/W] Regex for Negated set of any word (anything that's not A-Z

                            for (String word : a
                            ) {
                                //Catching Carriage return.
                                if (!word.isBlank()) {
                                    //Insert word into Trie.
                                    T.insert(word.toLowerCase());

                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("File not Found");
            }
        }
        System.out.println("Matching words");
        //Match words based of wildcard iteration
        T.setMatch();

        //PART 2 takes patten from in2.txt
        System.out.println("Loading Pattern");
        File filePattern = new File(in2);
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePattern));
            String str;
            while ((str = br.readLine()) != null) {
                str = str.toLowerCase();
                //temporary split of string looking for main chunk of word
                //e.g  ACT*R
                //Splits and takes ACT for wordmatch (speeds up if we know what node to begin with
                String[] temp = str.split("[^\\w]");
                T.wordMatch(temp[0],str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //OUTPUT HELPERS , Helpers are needed for calling writer situations with context, pass to Trie.java for output
        T.output1(out1);
        T.output2(out2);
    }
}
