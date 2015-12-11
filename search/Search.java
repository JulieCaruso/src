/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import model.Documents;
import model.Mots;
import org.jsoup.nodes.Document;

/**
 *
 * @author MC
 */
public class Search {

    /* recherche vectorielle pour tous les doc pour 1 req */
    public void vectorialSearch(ArrayList<String> corpusTitles, ArrayList<String> wordsReq, Documents docModel, Mots motModel) {
        // calcul de la dist de cos pour chaque doc/req (x docs, 1 req)
        // = |D & Q|/(sqrt(|D|)*sqrt(|Q|))
        // 
        //pour chq doc, iterer sur corpusTitles et a chq fois on appelle produit vect (doc, mot)
        int pv = 0; // le produit vectoriel
        for (String title : corpusTitles) {
            pv += produitVectorielDocMots(wordsReq, docModel, motModel, title);
        }
    }
    
}
