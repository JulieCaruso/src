/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author MC
 */
public class TextualInformation {

    public void generateWords(Document doc) {
        String text = doc.body().text();
        System.out.println(text);
    }

    public void removeEmptyWords(HashMap<String, String> emptyWords, ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (emptyWords.get(wordsList.get(i)) != null) {
                wordsList.remove(i);
            }
        }
    }

    public void truncate7(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (wordsList.get(i).length() > 7) {
                String truncated = wordsList.get(i).substring(0, 7);
                wordsList.set(i, truncated);
            }
        }
    }

    public void minimize(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            String minimized = wordsList.get(i).toLowerCase();
            wordsList.set(i, minimized);
        }
    }
}
