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
import parser.TextFrequency;
import db.DbConnection;
import java.sql.Connection;
import model.*;

/**
 *
 * @author Kapouter
 */
public class CinemaSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DbConnection dbConn = new DbConnection();
        dbConn.connectToDb();
        Connection conn = dbConn.getConnection();
        
        Documents docModel = new Documents(conn);
        Mots motModel = new Mots(conn);
        DocumentMot docMotModel = new DocumentMot(conn);
        
        FileParser p = new FileParser();
        TextualInformation ti = new TextualInformation();
        TextFrequency tf = new TextFrequency();
        
        ArrayList<Document> Corpus = p.parseCorpus(docModel);
        HashMap<String, String> EmptyWords = p.parseEmptyWords();
        
        ArrayList<ArrayList<String>> docsWordsList = ti.generateCorpusWords(Corpus);
        ArrayList<ArrayList<String>> cleanWordsList = ti.cleanCorpusWords(EmptyWords, docsWordsList);
     
        ti.insertCorpusWordsInDB(motModel, cleanWordsList);
        tf.insertDocMot(motModel, docModel, docMotModel, p.getCorpusTitles(), cleanWordsList);
        
        ArrayList<String> a = new ArrayList<>();
        a = cleanWordsList.get(0);
        for (String w : a){
            System.out.println(w);
        }
        
        dbConn.disconnectDb(conn);
    }
    
}
