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

    public ArrayList<String> reformulate(ArrayList<String> keyWordsReqList) {
        // copie des mots de la requete dans la liste qui va etre retournee
        ArrayList<String> refReqList = new ArrayList(keyWordsReqList);
        ArrayList<String> synReqList = new ArrayList<>();
        ArrayList<String> instReqList = new ArrayList<>();

        // ajout des synonymes
        synReqList = addSynonymousToReq(keyWordsReqList);
        for (String syn : synReqList) {
            if (refReqList.contains(syn) == false) {
                refReqList.add(syn);
            }
        }

        // ajout des instances
        instReqList = addInstancesToReq(keyWordsReqList);
        for (String inst : instReqList) {
            if (refReqList.contains(inst) == false) {
                refReqList.add(inst);
            }
        }

        return refReqList;
    }

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
        //System.out.println(synReqList.toString());
        return synReqList;
    }

    // TODO
    public ArrayList<String> addInstancesToReq(ArrayList<String> keyWordsReqList) {
        // copie des mots de la requete dans la liste qui va etre retournee
        ArrayList<String> instReqList = new ArrayList(keyWordsReqList);

        ArrayList<String> property = new ArrayList<>();;
        ArrayList<String> entity = new ArrayList<>();;
        ArrayList<String> instList = new ArrayList<>();

        // recherche des proprietes
        property = findProperty(keyWordsReqList);
        //System.out.println("proprietes : " + property);
        if (property != null) {
            // recherche des entites
            entity = findEntity(keyWordsReqList);
            //System.out.println("entites : " + entity);
            if (entity != null) {
                // alors on cherche les instances a ajouter a la requete
                for (String e : entity) {
                    for (String p : property) {
                        instList = getInstance(p, e);
                        //System.out.println("instances : " + instList);
                        // enrichissement de la requete
                        for (String inst : instList) {
                            if (instReqList.contains(inst) == false) {
                                instReqList.add(inst);
                            }
                        }
                    }
                }

            }
        }

        //System.out.println(instReqList.toString());
        return instReqList;
    }

    // -----------------------------   SPARQL PART  -------------------------------- //
    public ArrayList<String> getSynonymous(String word) {

        ArrayList<String> synList = new ArrayList<>();
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");

        // avec @fr
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
        
        // avec @en
        String query2 = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?label "
                + "WHERE {"
                + "?entite rdfs:label \"" + word + "\"@en."
                + "?entite rdfs:label ?label."
                + "}";
        Iterable<Map<String, String>> results2 = sparqlClient.select(query2);
        for (Map<String, String> m : results2) {
            synList.add(m.get("label"));
        }
        
        // avec aucun tag de langue
        String query3 = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?label "
                + "WHERE {"
                + "?entite rdfs:label \"" + word + "\"."
                + "?entite rdfs:label ?label."
                + "}";
        Iterable<Map<String, String>> results3 = sparqlClient.select(query3);
        for (Map<String, String> m : results3) {
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

        // dans l'autre sens
        String query2 = "PREFIX : <http://ontologies.alwaysdata.net/space#>"
                + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "SELECT ?labels "
                + "WHERE {"
                + "?propriete rdfs:label \"" + propriete + "\"@fr."
                + "?entite rdfs:label \"" + entite + "\"."
                + "?res ?propriete ?entite."
                + "?res rdfs:label ?labels."
                + "}";
        Iterable<Map<String, String>> results2 = sparqlClient.select(query2);

        for (Map<String, String> m : results2) {
            instList.add(m.get("labels"));
        }
        //System.out.println("instances " + instList.toString());
        return instList;
    }

    // Si un ou plusieurs termes de la requete sont des proprietes, alors il sont retournes
    public ArrayList<String> findProperty(ArrayList<String> keyWordsReqList) {
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
        ArrayList<String> property = new ArrayList<>();
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
                property.add(keyWord);
            }
        }
        // System.out.println(property);
        return property;
    }

    // Si l'un des termes de la requete est une entite, alors il est retourne (null sinon)
    public ArrayList<String> findEntity(ArrayList<String> keyWordsReqList) {
        SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
        ArrayList<String> entity = new ArrayList<>();
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
                entity.add(keyWord);
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
