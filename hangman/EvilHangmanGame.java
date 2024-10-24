package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    Set<String> wordSet = new HashSet<>();
    SortedSet<Character> usedChars = new TreeSet<>();
    StringBuilder revealedWord;
    @Override
    public void startGame(File dicFile, int wordLength) throws IOException, EmptyDictionaryException {
        revealedWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            revealedWord.append('_');
        }
        Scanner dicScan = new Scanner(dicFile);
        while (dicScan.hasNext()) {
            wordSet.add(dicScan.next());
        }
        Set<String> newWordSet = new HashSet<>();
        Iterator<String> it = wordSet.iterator();
        while (it.hasNext()) {
            String checkWord = it.next();
            if (checkWord.length() == wordLength) {
                newWordSet.add(checkWord);
            }
        }
        wordSet = newWordSet;
        if (wordSet.size() == 0) {
            throw new EmptyDictionaryException();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        Map<String, Set<String>> subsets = new HashMap<>();
        if (usedChars.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }
        usedChars.add(guess);
        TreeSet<String> keys = new TreeSet<>();
        Iterator<String> it = wordSet.iterator();
        while (it.hasNext()) {
            String word = it.next();
            String subsetKey = word;
            for (int i = 0; i < subsetKey.length(); i++) {
                if (subsetKey.charAt(i) != guess) {
                    subsetKey = subsetKey.substring(0,i) + '_' + subsetKey.substring(i+1, subsetKey.length());
                }
            }
            if (!subsets.containsKey(subsetKey)) {
                subsets.put(subsetKey, new HashSet<>());
                keys.add(subsetKey);
            }
            subsets.get(subsetKey).add(word);
        }
        int largestSize = 0;
        int ties = 1;
        TreeSet<String> largestKeys = new TreeSet<>();
        Iterator<String> it2 = keys.iterator();
        while (it2.hasNext()) {
            String key = it2.next();
            if (subsets.get(key).size() > largestSize) {
                largestSize = subsets.get(key).size();
                largestKeys.clear();
                largestKeys.add(key);
                ties = 1;
            }
            else if (subsets.get(key).size() == largestSize) {
                largestKeys.add(key);
                ties++;
            }
        }
        if (ties == 1) {
            for (int i = 0; i < revealedWord.length(); i++) {
                if (largestKeys.first().charAt(i) == guess) {
                    revealedWord = revealedWord.replace(i, i+1, largestKeys.first().substring(i, i+1));
                }
            }
            wordSet = subsets.get(largestKeys.first());
        }
        else {
            Iterator<String> it3 = largestKeys.iterator();
            int leastChars = revealedWord.length();
            TreeSet<String> smallestLargestKeys = new TreeSet<>();
            smallestLargestKeys.add(largestKeys.first());
            while (it3.hasNext()) {
                String currKey = it3.next();
                int charsGiven = 0;
                for (int i = 0; i < currKey.length(); i++) {
                    if (currKey.charAt(i) == guess) {
                        charsGiven++;
                    }
                }
                if (charsGiven < leastChars) {
                    leastChars = charsGiven;
                    smallestLargestKeys.clear();
                    smallestLargestKeys.add(currKey);
                }
                else if (charsGiven == leastChars) {
                    smallestLargestKeys.add(currKey);
                }
            }
            for (int i = 0; i < revealedWord.length(); i++) {
                if (smallestLargestKeys.first().charAt(i) == guess) {
                    revealedWord = revealedWord.replace(i, i+1, smallestLargestKeys.first().substring(i, i+1));
                }
            }
            wordSet = subsets.get(smallestLargestKeys.first());
        }
        return wordSet;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return usedChars;
    }

    public String getRevealedWord() {
        return revealedWord.toString();
    }
}
