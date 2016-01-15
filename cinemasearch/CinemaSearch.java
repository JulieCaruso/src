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
import java.text.DecimalFormat;
import java.util.Map;
import model.*;
import parser.Evaluator;
import parser.HtmlParser;
import reformulator.Reformulator;
import search.Search;
import sparqlclient.SparqlClientExample;

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
       /* DbConnection dbConn = new DbConnection();
        dbConn.connectToDb();
        Connection conn = dbConn.getConnection();

        FileParser p = new FileParser();
        TextualInformation ti = new TextualInformation();
        Evaluator e = new Evaluator();
        Documents docModel = new Documents(conn);
        Tf tfModel = new Tf(conn);

        TextFrequency tf = new TextFrequency();
        Search s = new Search(ti);
        // Parsing corpus et empty words
        HashMap<String, String> EmptyWords = p.parseEmptyWords();

       //chargementDonneesDB(conn, EmptyWords, p, ti, tfModel, docModel); */

         // essai html parser 
         FileParser p = new FileParser();
         HtmlParser hp = new HtmlParser(); 
         Document d = p.parseFile("requetes.html");
         hp.generateWords(d);
         Reformulator r = new Reformulator();
         r.synonymous("prix");
        // essai requete
      /*  String req = "Quelles sont les personnes impliquées dans le film Intouchables?";
        ArrayList<String> rq = ti.parseRequete(EmptyWords, req);
        HashMap<String, Double> cosDoc = s.vectorialSearch(docModel.getCorpusTitles(), rq, tfModel, docModel);
        HashMap<String, Integer> pertDoc = s.pertinence(cosDoc);
        System.out.println("Taille du corpusDoc : " + docModel.getCorpusTitles().size());
        System.out.println("Taille de la map cosDoc : " + cosDoc.size());
        System.out.println("Taille de la map pertDoc : " + pertDoc.size());
        for (Map.Entry<String, Integer> entry : pertDoc.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        Double percentage = e.evaluate(pertDoc, 1);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        System.out.println("Pertinence de résultat de la requête : " + numberFormat.format(percentage * 100) + "%");

        dbConn.disconnectDb(conn);
    }

    public static void chargementDonneesDB(Connection conn, HashMap<String, String> EmptyWords, FileParser p, TextualInformation ti, Tf tfModel, Documents docModel) {
        // création des modèles des tables
        Mots motModel = new Mots(conn);
        DocumentMot docMotModel = new DocumentMot(conn);
        motModel.truncate();
        docMotModel.truncate();
        docModel.truncate();
        tfModel.truncate();

        // instanciation des outils
        TextFrequency tf = new TextFrequency();
        // Parsing corpus et empty words
        ArrayList<Document> Corpus = p.parseCorpus(docModel);

        // Parsing et nettoyage des mots du corpus
        ArrayList<ArrayList<String>> docsWordsList = ti.generateCorpusWords(Corpus);
        ArrayList<ArrayList<String>> cleanWordsList = ti.cleanCorpusWords(EmptyWords, docsWordsList);

        //Ajout des mots du corpus et du lien doc-mot en base de données
        //ti.insertCorpusWordsInDB(motModel, cleanWordsList);
        tf.insertDocMot(motModel, docModel, docMotModel, tfModel, p.getCorpusTitles(), cleanWordsList); */
    }

}
