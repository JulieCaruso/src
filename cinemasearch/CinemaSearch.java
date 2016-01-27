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
import parser.Evaluator;
import parser.HtmlParser;
import reformulator.Reformulator;
import search.Search;

public class CinemaSearch {

    // commence à 0, termine à 8
    private static final int idRequete = 7;
    // 0 : modele vectoriel, 1 : modele vectoriel pondere par idf
    private static final int methode = 0;
    // 0 : pas de reformulation des requêtes, 1 sinon
    private static final int reformulation = 0;
    private static final String fichierRequetes = "requetes.html";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // connection base de données
        DbConnection dbConn = new DbConnection();
        dbConn.connectToDb();
        Connection conn = dbConn.getConnection();
        // initialisation outils
        FileParser p = new FileParser();
        TextualInformation ti = new TextualInformation();
        Evaluator e = new Evaluator();
        Documents docModel = new Documents(conn);
        Tf tfModel = new Tf(conn);
        TextFrequency tf = new TextFrequency();
        Search s = new Search(ti);
        HtmlParser hp = new HtmlParser();
        Reformulator r = new Reformulator();

        // Parsing corpus et empty words
        HashMap<String, String> EmptyWords = p.parseEmptyWords();

        // chargement des mots dans la db
        // chargementDonneesDB(conn, EmptyWords, p, ti, tfModel, docModel);  
        // recupération requete et traitement
        Document d = p.parseFile(fichierRequetes);

        // generation de la requete parsee, avec ou sans reformulation
        ArrayList<ArrayList<String>> rq = hp.generateWords(d);
        ArrayList<String> reqFinal = new ArrayList();
        if (reformulation == 0) {
            reqFinal = ti.parseRequete(EmptyWords, String.join(" ", rq.get(idRequete)));
            System.out.println("Requete sans reformulation : " + rq.get(idRequete).toString());
            System.out.println("Requete parsée : " + reqFinal);
        } else if (reformulation == 1) {
            ArrayList<String> refReqList = r.reformulate(rq.get(idRequete));
            reqFinal = ti.parseRequete(EmptyWords, String.join(" ", rq.get(idRequete)));
            System.out.println("Requete avant reformulation : " + rq.get(idRequete).toString());
            System.out.println("Requete après reformulation : " + refReqList.toString());
            System.out.println("Requete parsée : " + reqFinal);
        }

        // calcul pertinence des documents
        HashMap< String, Double> cosDoc = new HashMap<>();
        // calcul vectoriel
        if (methode == 0) {
            System.out.println("Modele vectoriel, requete " + (idRequete + 1));
            cosDoc = s.vectorialSearch(docModel.getCorpusTitles(), reqFinal, tfModel, docModel);
        } // calcul vectoriel pondéré avec idf
        else if (methode == 1) {
            System.out.println("Modele vectoriel pondere par idf, requete " + (idRequete + 1));
            cosDoc = s.vectorialSearchIdf(docModel.getCorpusTitles(), reqFinal, tfModel, docModel);
        }

        HashMap<String, Integer> pertDoc = s.pertinence(cosDoc, 0.6);
        // evaluation de la requete
        e.evaluate(pertDoc, idRequete + 1);

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
        tf.insertDocMot(motModel, docModel, docMotModel, tfModel, p.getCorpusTitles(), cleanWordsList);
    }

}
