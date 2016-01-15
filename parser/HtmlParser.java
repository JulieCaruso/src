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
public class HtmlParser {

    private final String requetesFile = "requetes.html";

    public ArrayList<String> generateWords(Document doc) {
        ArrayList<String> keyWordsReqList = new ArrayList();

        Element body = doc.body();
        Elements elementsBody = body.getAllElements();
        for (Element e : elementsBody) {
            //recup tous les dl et pour chq dl recup les 2 dt a mettre dans liste requete
            if (e.nodeName().equals("dl")) {
                keyWordsReqList.add(e.child(1).text());
            }
        }
        return keyWordsReqList;
    }
}
