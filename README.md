# WordMatch_Trie
Application To read any number of text files and store information in a Trie Tree

A word by definition of the project was any String without punctuation.

With requirements of the application to Identify all Words within any number of provided textfiles
Obtain the Count or occurance of words (number of occurance of the word)
Matching or Similar words (Any word within 1 character diffrence found by Wildcard matching on ?)
E.G. CAT is similar to  BAT, COT , SAT.

Sepreatly the output of All words in the structure matching a wildcard pattern
"?" = any single character.
"*" = any number of characters including an Empty character.


A Trie was the Chosen implementation of the word structure given it's a tree based structure that is specific for characters
Any interaction with the Trie is of O[NxM] N= number of words and M= Average length of all words.
Due to this all interactions are O[N]
