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

    public ArrayList<String> addSynonymousToReq(ArrayList<String> keyWordsReqList) {
        // copie des mots de la requete dans la liste qui va etre retournee
        ArrayList<String> synReqList = new ArrayList(keyWordsReqList);

        // recherche des synonymes
        ArrayList<String> synList = new ArrayList();
        for (String word : keyWordsReqList) {
            synList.addAll(getSynonymous(word));
        }

        // construction de la liste retournee en ajoutant les synonymes a la liste de mots de depart
        for (String syn : synList) {
            if (synReqList.contains(syn) == false) {
                synReqList.add(syn);
            }
        }
        System.out.println(synReqList.toString());
        return synReqList;
    }

    // TODO
    public ArrayList<String> addInstancesToReq(ArrayList<String> keyWordsReqList) {
        // copie des mots de la requete dans la liste qui va etre retournee
        ArrayList<String> instReqList = new ArrayList(keyWordsReqList);

        String property = null;
        String entity = null;
        ArrayList<String> instList = new ArrayList();

        // si la requete contient exactement 2 mots
        if (keyWordsReqList.size() == 2) {
            // si l'un d'eux est une propriete
            property = findProperty(keyWordsReqList);
            if (property != null) {
                // si l'autre est une entite
                entity = findEntity(keyWordsReqList);
                if (entity != null) {
                    // alors on cherche l'instance (les instances) a ajouter a la requete
                    instList = getInstance(property, entity);

                    // enrichissement de la requete
                    for (String inst : instList) {
                        if (instReqList.contains(inst) == false) {
                            instReqList.add(inst);
                        }
                    }
                }
            }
        }

        System.out.println(instReqList.toString());
        return instReqList;
    }

    // -----------------------------   SPARQL PART  -------------------------------- //
    public ArrayList<String> getSynonymous(String word) {

        ArrayList<String> synList = new ArrayList<>();
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");

        String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?label "
                + "WHERE {"
                + "?entite rdfs:label \"" + word + "\"@fr."
                + "?entite rdfs:label ?label."
                + "}";
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        for (Map<String, String> m : results) {
            synList.add(m.get("label"));
        }
        //System.out.println(synList.toString());
        return synList;
    }

    // Exemple : "lieu de naissance, Omar Sy" retourne "Trappes"
    public ArrayList<String> getInstance(String propriete, String entite) {
        ArrayList<String> instList = new ArrayList<>();
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");

        // Pour les instances : fonctionne
        String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?labels "
                + "WHERE {"
                + "?propriete rdfs:label \"" + propriete + "\"@fr."
                + "?entite rdfs:label \"" + entite + "\"."
                + "?entite ?propriete ?res."
                + "?res rdfs:label ?labels."
                + "}";
        Iterable<Map<String, String>> results = sparqlClient.select(query);

        for (Map<String, String> m : results) {
            instList.add(m.get("labels"));
        }
        //System.out.println(instList.toString());
        return instList;
    }

    // Si l'un des termes de la requete est une propriete, alors il est retourne (null sinon)
    public String findProperty(ArrayList<String> keyWordsReqList) {
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
        String property = null;
        for (String keyWord : keyWordsReqList) {
            // System.out.println("key :" + keyWord);
            String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                    + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                    + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                    + "ASK WHERE {"
                    + "?propriete rdfs:label \"" + keyWord + "\"@fr."
                    + "?entite ?propriete ?res."
                    + "}";
            boolean found = sparqlClient.ask(query);
            if (found) {
                property = keyWord;
            }
        }
        // System.out.println(property);
        return property;
    }

    // Si l'un des termes de la requete est une propriete, alors il est retourne (null sinon)
    public String findEntity(ArrayList<String> keyWordsReqList) {
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
        String entity = null;
        for (String keyWord : keyWordsReqList) {
            // System.out.println("key :" + keyWord);
            String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                    + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                    + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                    + "ASK WHERE {"
                    + "?entite rdfs:label \"" + keyWord + "\"."
                    + "?entite ?propriete ?res."
                    + "}";
            boolean found = sparqlClient.ask(query);
            if (found) {
                entity = keyWord;
            }
        }
        // System.out.println(entity);
        return entity;
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
