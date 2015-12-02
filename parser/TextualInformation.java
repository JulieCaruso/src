/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author MC
 */
public class TextualInformation {

    public void generateWords(Document doc) {
        ArrayList<String> textList = new ArrayList();
        String aux = "";
        Element head = doc.head();
        Elements elements = head.getAllElements();
        for (Element e : elements) {
            aux = e.text();
            textList.add(aux);
        }

        System.out.println(textList.toString());
    }
}
