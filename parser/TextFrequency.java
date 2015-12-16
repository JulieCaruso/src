/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.HashMap;
import model.DocumentMot;
import model.Documents;
import model.Mots;
import model.Tf;

/**
 *
 * @author Kapouter
 */
public class TextFrequency {

    ArrayList<HashMap<String, Integer>> tfMap;

    public TextFrequency() {
        this.tfMap = new ArrayList<>();
    }

    /**
     * Créé la map des tfs pour chaque mot de chaque document à partir de la liste des mots
     * @param docsWordsList liste des mots de chaque document
     */
    private void createTfMap(ArrayList<ArrayList<String>> docsWordsList) {
        for (ArrayList<String> wordsList : docsWordsList) {
            HashMap<String, Integer> wordsMap = new HashMap<>();
            for (String word : wordsList) {
                Integer tf = wordsMap.get(word);
                if (tf == null) {
                    wordsMap.put(word, 1);
                } else {
                    wordsMap.put(word, tf+1);
                }
            }
            this.tfMap.add(wordsMap);
        }
    }
    
    /**
     * Insère un document_mot dans la table
     * @param motModel
     * @param docModel
     * @param docMotModel
     * @param CorpusTitles titres des documents du corpus
     * @param docsWordsList liste des mots de chaque documents
     */
    public void insertDocMot (Mots motModel, Documents docModel, DocumentMot docMotModel, Tf tfModel, ArrayList<String> CorpusTitles, ArrayList<ArrayList<String>> docsWordsList) {
        createTfMap(docsWordsList);
        for (int i = 0; i < this.tfMap.size(); i++) {
            HashMap<String, Integer> wordsMap = tfMap.get(i);
            for (String mot : wordsMap.keySet()) {
                int id_mot = motModel.getId(mot);
                int id_document = docModel.getId(CorpusTitles.get(i));
                //docMotModel.insert(id_document, id_mot, wordsMap.get(mot));
                tfModel.insert(id_document, mot, wordsMap.get(mot));
            }
        }
        
        
        
    }

}
