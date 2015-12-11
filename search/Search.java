/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.HashMap;
import model.Documents;
import model.Mots;
import model.DocumentMot;

/**
 *
 * @author MC
 */
public class Search {

    /* recherche vectorielle pour tous les doc pour 1 req */
    public HashMap<String,Double> vectorialSearch(ArrayList<String> corpusTitles, ArrayList<String> wordsReq, Documents docModel, Mots motModel, DocumentMot docMotModel) {
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
            pv = docMotModel.produitVectorielDocMots(wordsReq, docModel, motModel, title);
            tfDoc = docMotModel.getTfDoc(title, docModel);
            cos = pv/(sqrt(tfDoc)*sqrt(sizeReq));
            cosDoc.put(title, cos);
        }
        return cosDoc;
    }
    
}
