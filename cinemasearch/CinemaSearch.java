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
import search.Search;

/**
 *
 * @author Kapouter
 */
public class CinemaSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // connection base de données
        DbConnection dbConn = new DbConnection();
        dbConn.connectToDb();
        Connection conn = dbConn.getConnection();
        
        FileParser p = new FileParser();
        TextualInformation ti = new TextualInformation();
        Search s = new Search();
        HashMap<String, String> EmptyWords = p.parseEmptyWords();

        // essai requete
        String req = "Quelles sont les personnes impliquées dans le film Intouchables?";
        ArrayList<String> rq = ti.parseRequete(EmptyWords, req);
        for (String w : rq) {
            System.out.println(w);
        }
//        HashMap<String,Double> cosDoc = s.vectorialSearch(p.getCorpusTitles(), rq, docModel, motModel, docMotModel);
//        System.out.println(cosDoc);

        dbConn.disconnectDb(conn);
    }

    public void chargementDonneesDB(Connection conn, HashMap<String, String> EmptyWords, FileParser p, TextualInformation ti) {
        // création des modèles des tables
        Documents docModel = new Documents(conn);
        Mots motModel = new Mots(conn);
        DocumentMot docMotModel = new DocumentMot(conn);
        // instanciation des outils
        
        TextFrequency tf = new TextFrequency();
        Search s = new Search();
        // Parsing corpus et empty words
        ArrayList<Document> Corpus = p.parseCorpus(docModel);

        // Parsing et nettoyage des mots du corpus
        ArrayList<ArrayList<String>> docsWordsList = ti.generateCorpusWords(Corpus);
        ArrayList<ArrayList<String>> cleanWordsList = ti.cleanCorpusWords(EmptyWords, docsWordsList);

        //Ajout des mots du corpus et du lien doc-mot en base de données
        ti.insertCorpusWordsInDB(motModel, cleanWordsList);
        tf.insertDocMot(motModel, docModel, docMotModel, p.getCorpusTitles(), cleanWordsList);
    }

}
