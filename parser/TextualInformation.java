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
}
