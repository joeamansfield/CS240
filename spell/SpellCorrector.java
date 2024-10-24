package spell;

import java.io.IOException;
import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector{
    private ITrie dictionary = new Trie();
    public void useDictionary(String dictionaryFileName) throws IOException {
        File dicFile = new File(dictionaryFileName);
        Scanner dicScan = new Scanner(dicFile);
        while (dicScan.hasNext()) {
            String word = dicScan.next();
            word = word.toLowerCase();
            dictionary.add(word);
        }
    }

    public String suggestSimilarWord(String inputWord) {
        String word = inputWord.toLowerCase();
        if (dictionary.find(word) != null) {
            return word;
        }
        ArrayList<String> suggestionList = new ArrayList<>();
        ArrayList<String> refinedSuggestionList = new ArrayList<>();
        suggestionList.addAll(delete(word));
        suggestionList.addAll(transpose(word));
        suggestionList.addAll(alter(word));
        suggestionList.addAll(insert(word));
        Iterator<String> it = suggestionList.iterator();
        int maxSuggestion = 0;
        while (it.hasNext()) {
            String suggestion = it.next();
            INode dicNode = dictionary.find(suggestion);
            if (dicNode != null) {
                if (dicNode.getValue() > maxSuggestion) {
                    refinedSuggestionList.clear();
                    refinedSuggestionList.add(suggestion);
                    maxSuggestion = dicNode.getValue();
                }
                else if (dicNode.getValue() == maxSuggestion) {
                    refinedSuggestionList.add(suggestion);
                }
            }
        }
        if (maxSuggestion > 0) {
            Comparator<String> cmp = Comparator.naturalOrder();
            refinedSuggestionList.sort(cmp);
            return refinedSuggestionList.get(0);
        }
        ArrayList<String> suggestionListMKII = new ArrayList<>();
        it = suggestionList.iterator();
        while (it.hasNext()) {
            String suggestion = it.next();
            suggestionListMKII.addAll(delete(suggestion));
            suggestionListMKII.addAll(transpose(suggestion));
            suggestionListMKII.addAll(alter(suggestion));
            suggestionListMKII.addAll(insert(suggestion));
        }
        it = suggestionListMKII.iterator();
        while (it.hasNext()) {
            String suggestion = it.next();
            INode dicNode = dictionary.find(suggestion);
            if (dicNode != null) {
                if (dicNode.getValue() > maxSuggestion) {
                    refinedSuggestionList.clear();
                    refinedSuggestionList.add(suggestion);
                    maxSuggestion = dicNode.getValue();
                }
                else if (dicNode.getValue() == maxSuggestion) {
                    refinedSuggestionList.add(suggestion);
                }
            }
        }
        if (maxSuggestion > 0) {
            Comparator<String> cmp = Comparator.naturalOrder();
            refinedSuggestionList.sort(cmp);
            return refinedSuggestionList.get(0);
        }
        return null;
    }

    private ArrayList<String> delete(String word) {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            String suggestion = word.substring(0,i) + word.substring(i+1);
            output.add(suggestion);
        }
        return output;
    }

    private ArrayList<String> transpose(String word) {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < word.length() - 1; i++) {
            String suggestion = word.substring(0,i) + word.charAt(i+1) + word.charAt(i) + word.substring(i+2);
            output.add(suggestion);
        }
        return output;
    }

    private ArrayList<String> alter(String word) {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char newChar = (char)(j + 'a');
                String suggestion = word.substring(0, i) + newChar + word.substring(i+1);
                output.add(suggestion);
            }
        }
        return output;
    }

    private ArrayList<String> insert(String word) {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i <= word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char newChar = (char)(j + 'a');
                String suggestion = word.substring(0, i) + newChar + word.substring(i);
                output.add(suggestion);
            }
        }
        return output;
    }
}