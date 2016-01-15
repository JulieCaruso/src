/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reformulator;

import java.util.ArrayList;
import java.util.Map;
import sparqlclient.SparqlClient;

/**
 *
 * @author MC
 */
public class Reformulator {
    // Prend en entree une liste de requetes sous forme de mots cles
    // et fournit en sortie la liste des resquetes reformulees

    private ArrayList<String> reformReqList = new ArrayList();

    private ArrayList<String> findSynonymous(ArrayList<String> keyWordsReqList) {
        ArrayList<String> synReqList = new ArrayList();

        return synReqList;
    }

    // -----------------------------   SPARQL PART  -------------------------------- //
    public void synonymous(String word) {
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");

        // Pour les instances : fonctionne
        /*String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?labels WHERE { ?propriete rdfs:label \"lieu naissance\"@fr."
                + "?entite rdfs:label \"Omar Sy\"."
                + "  ?entite ?propriete ?res."
                + "  ?res rdfs:label ?labels."
                + "}";
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        // nbPersonnesParPiece(sparqlClient);;
        for (Map<String, String> m : results) {
            System.out.println(m.toString());
        }*/
        String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?label"
                + "WHERE {"
                + "?entite rdfs:label \"" + word + "\"@fr."
                + "?entite rdfs:label ?label."
                + "}";
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        // nbPersonnesParPiece(sparqlClient);;
        for (Map<String, String> m : results) {
            System.out.println(m.toString());
        }
        //--------------- Affiche bien 5 resultats mais vides... --------------
    }

    
    
    /*private static void nbPersonnesParPiece(SparqlClient sparqlClient) {
        String query = "PREFIX : <http://www.lamaisondumeurtre.fr#>\n"
                + "SELECT ?piece (COUNT(?personne) AS ?nbPers) WHERE\n"
                + "{\n"
                + "    ?personne :personneDansPiece ?piece.\n"
                + "}\n"
                + "GROUP BY ?piece\n";
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        System.out.println("nombre de personnes par pièce:");
        for (Map<String, String> result : results) {
            System.out.println(result.get("piece") + " : " + result.get("nbPers"));
        }
    }*/
}
