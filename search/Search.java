/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import model.Documents;
import model.Mots;
import model.DocumentMot;
import model.Tf;
import parser.TextualInformation;

/**
 *
 * @author MC
 */
public class Search {

    private TextualInformation ti ; 
    
    /* constructor */
    public Search(TextualInformation ti) {
        this.ti = ti;
    }
    
    /* recherche vectorielle pour tous les doc pour 1 req */
    public HashMap<String,Double> vectorialSearch(ArrayList<String> corpusTitles, ArrayList<String> wordsReq, Tf tfModel, Documents docModel) {
        // calcul de la dist de cos pour chaque doc/req (x docs, 1 req)
        // = |D & Q|/(sqrt(|D|)*sqrt(|Q|))
        // 
        //pour chq doc, iterer sur corpusTitles et a chq fois on appelle produit vect (doc, mot)
        HashMap<String,Double> cosDoc = new HashMap<>(); 
        int pv = 0;
        int tfDoc = 0;
        int sizeReq = wordsReq.size();
        double cos = 0;
        for (String title : corpusTitles) {
            pv = tfModel.produitVectorielDocMots(wordsReq, docModel, title);
            tfDoc = tfModel.getTfDoc(title, docModel);
            cos = pv/(sqrt(tfDoc)*sqrt(sizeReq));
            cosDoc.put(title, cos);
        }
        return cosDoc;
    }
    
    /* Gives the pertinence of all the documents in a HashMap */ 
    public HashMap<String,Integer> pertinence(HashMap<String,Double> cosDoc) {
        HashMap<String,Integer> pertDoc = new HashMap<>();
        for (Entry<String, Double> entry : cosDoc.entrySet()) {
            if (entry.getValue() >= 0.5) {
                pertDoc.put(entry.getKey(), 1);
            } else {
                pertDoc.put(entry.getKey(), 0);
            }
        }
        return pertDoc;
    }

    // Probleme : besoin du corpusTitles !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Exporter résultat dans un fichier txt ???????????????????????
    
    /*public HashMap<String,Integer> resolveQuery (String query, ) {
        
    }*/
    
}
