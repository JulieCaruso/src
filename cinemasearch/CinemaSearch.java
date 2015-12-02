/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemasearch;

import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.nodes.Document;
import parser.FileParser;
import parser.TextualInformation;

/**
 *
 * @author Kapouter
 */
public class CinemaSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileParser p = new FileParser();
        TextualInformation t = new TextualInformation();
        
        ArrayList<Document> Corpus = p.parseCorpus();
        HashMap<String, String> EmptyWords = p.parseEmptyWords();
        TextualInformation ti = new TextualInformation();
        ti.generateWords(Corpus.get(0));
        
        ArrayList<String> a = new ArrayList<>();
        a.add("Cheval,");
        a.add(",");
        a.add("chEvAline");
        a.add("chevalinette");
        t.truncate7(a);
        t.minimize(a);
        t.removePunctuation(a);
        for (String w : a){
            System.out.println(w);
        }
    }
    
}
