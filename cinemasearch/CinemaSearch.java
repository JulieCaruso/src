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
        TextualInformation ti = new TextualInformation();
        
        ArrayList<Document> Corpus = p.parseCorpus();
        HashMap<String, String> EmptyWords = p.parseEmptyWords();
        
        ArrayList<String> a = new ArrayList<>();
        a.add("Cheval,");
        a.add(",");
        a.add("chEvAline");
        a.add("chevalinette");
        
        a = ti.generateWords(Corpus.get(0));
        ti.truncate7(a);
        ti.minimize(a);
        ti.removePunctuation(a);
        for (String w : a){
            System.out.println(w);
        }
        // le - ????§§§!!!!
    }
    
}
