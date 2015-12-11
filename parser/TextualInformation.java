/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import model.Mots;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author MC
 */
public class TextualInformation {

    // pattern pour regexp
    private static final Pattern PUNCTUATION = Pattern.compile("[\\]\\[(){} ,.;\\-:!?&@<>|'%€–/=©]");

    private ArrayList<ArrayList<String>> CorpusWords;

    /* constructeur*/
    public TextualInformation() {
        this.CorpusWords = new ArrayList<>();
    }

    /**
     * Retourne tous les mots d'un corpus non nettoyés
     *
     * @param corpus
     * @return liste des mots par document
     */
    public ArrayList<ArrayList<String>> generateCorpusWords(ArrayList<Document> corpus) {
        for (Document document : corpus) {
            this.CorpusWords.add(generateWords(document));
        }
        return this.CorpusWords;
    }

    /**
     * Generation de la liste de mots pour un document
     *
     * @param doc
     * @return liste des mots
     */
    public ArrayList<String> generateWords(Document doc) {
        ArrayList<String> wordsList = new ArrayList();

        Element head = doc.head();
        Elements elementsHead = head.getAllElements();
        for (Element e : elementsHead) {
            wordsList.addAll(splitWords(e));
        }
        Element body = doc.body();
        Elements elementsBody = body.getAllElements();
        for (Element e : elementsBody) {
            wordsList.addAll(splitWords(e));
        }
        return wordsList;
    }

    /**
     * Separation des mots en fonction de la ponctuation
     * @param e
     * @return liste des mots
     */
    public ArrayList<String> splitWords(Element e) {
        ArrayList<String> wordsList = new ArrayList();
        String aux = "";
        if (e.nodeName().equals("meta")) {
            aux = e.attr("content");
            String auxTab[] = aux.split(PUNCTUATION.toString());
            for (String w : auxTab) {
                if (w.length() > 0) {
                    wordsList.add(w);
                }
            }
        } else {
            aux = e.text();
            String auxTab[] = aux.split(PUNCTUATION.toString());
            for (String w : auxTab) {
                if (w.length() > 0) {
                    wordsList.add(w);
                }
            }
        }
        return wordsList;
    }

    /**
     * Retourne le corpus nettoye de tous les mots
     *
     * @param emptyWords hashmap des mots vides
     * @param corpusWords liste des mots par document
     * @return listes des mots nettoyés par document
     */
    public ArrayList<ArrayList<String>> cleanCorpusWords(HashMap<String, String> emptyWords, ArrayList<ArrayList<String>> corpusWords) {
        for (ArrayList<String> documentWords : corpusWords) {
            minimize(documentWords);
            removeEmptyWords(emptyWords, documentWords);
            truncate7(documentWords);
        }
        return corpusWords;
    }

    public void removeEmptyWords(HashMap<String, String> emptyWords, ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (emptyWords.get(wordsList.get(i)) != null) {
                wordsList.remove(i);
            }
        }
    }

    public void truncate7(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (wordsList.get(i).length() > 7) {
                String truncated = wordsList.get(i).substring(0, 7);
                wordsList.set(i, truncated);
            }
        }
    }

    public void minimize(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            String minimized = wordsList.get(i).toLowerCase();
            wordsList.set(i, minimized);
        }
    }

    public void removePunctuation(ArrayList<String> wordsList) {
        for (int i = 0; i < wordsList.size(); i++) {
            String punctuationless = PUNCTUATION.matcher(wordsList.get(i)).replaceAll("");
            if (punctuationless.length() > 0) {
                wordsList.set(i, punctuationless);
            } else {
                wordsList.remove(i);
            }
        }
    }

    /**
     * Insere le corpus nettoye de mots dans la base de donnees
     *
     * @param motsDB
     * @param corpusWords
     */
    public void insertCorpusWordsInDB(Mots motsDB, ArrayList<ArrayList<String>> corpusWords) {
        for (int i = 0; i < corpusWords.size(); i++) {
            ArrayList<String> documentWords = corpusWords.get(i);
            for (String words : documentWords) {
                motsDB.insertIfNotPresent(words);
            }
        }
    }

    /**
     * Parse la requete et retourne une liste de mots
     * @param emptyWords hashmap des mots vides
     * @param req requete
     * @return liste des mots de la requete
     */
    public ArrayList<String> parseRequete(HashMap<String, String> emptyWords, String req) {
        ArrayList<String> rq = new ArrayList<>();
        String[] r = req.split(" ");
        for (String word : r) {
            rq.add(word);
        }
        removePunctuation(rq);
        minimize(rq);
        removeEmptyWords(emptyWords, rq);
        truncate7(rq);
        return rq;
    }

}
