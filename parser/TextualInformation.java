/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
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
}
