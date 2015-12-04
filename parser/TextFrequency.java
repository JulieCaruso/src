/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kapouter
 */
public class TextFrequency {

    ArrayList<HashMap<String, Integer>> tfMap;

    public TextFrequency() {
        this.tfMap = new ArrayList<>();
    }

    public void createTfMap(ArrayList<ArrayList<String>> docsWordsList) {
        for (ArrayList<String> wordsList : docsWordsList) {
            HashMap<String, Integer> wordsMap = new HashMap<>();
            for (String word : wordsList) {
                Integer tf = wordsMap.get(word);
                if (tf == null) {
                    wordsMap.put(word, 1);
                } else {
                    wordsMap.put(word, tf+1);
                }
            }
            this.tfMap.add(wordsMap);
        }
    }

}
