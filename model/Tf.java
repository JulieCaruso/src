/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import static java.lang.Math.log;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kapouter
 */
public class Tf {

    private Connection conn;

    public Tf(Connection connection) {
        this.conn = connection;
    }

    /**
     *
     * @param doc_name nom du document
     * @param mot
     * @param tf
     */
    public void insert(int id_doc, String mot, int tf) {
        PreparedStatement stmt = null;
        String sql = null;
        try {
            stmt = conn.prepareStatement("insert into tf (id_document, mot, tf) values(?, ?, ?)");
            stmt.setInt(1, id_doc);
            stmt.setString(2, mot);
            stmt.setInt(3, tf);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Documents.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
    }

    /**
     * Retourne le tf de la ligne
     *
     * @param id_document
     * @param mot
     * @return tf
     */
    public int getTf(int id_document, String mot) {
        int tf = 0;
        Statement stmt = null;
        String sql = null;
        try {
//            stmt = conn.prepareStatement("select * from tf where id_document, mot like (?, ?)");
//            stmt.setInt(1, id_document);
//            stmt.setString(2, mot);
//            ResultSet rs = stmt.executeQuery();
//            stmt.close();
            stmt = conn.createStatement();
            sql = "SELECT * FROM tf WHERE id_document = " + id_document + " and mot = '" + mot + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                tf = rs.getInt("tf");
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Mots.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
        return tf;
    }

    /**
     * On ajoute tous les tfs des mots d'une requête contenus dans un document
     *
     * @param mots liste des mots d'une requete
     * @param docModel modele de la table documents
     * @param doc_name nom du document
     * @return somme des tfs
     */
    public int produitVectorielDocMots(ArrayList<String> mots, Documents docModel, String doc_name) {
        int result = 0;
        for (String mot : mots) {
            int id_document = docModel.getId(doc_name);
            result += getTf(id_document, mot);
        }
        return result;
    }

    /**
     * On ajoute tous les tfs des mots d'une requête contenus dans un document
     *
     * @param mots liste des mots d'une requete
     * @param docModel modele de la table documents
     * @param doc_name nom du document
     * @return somme des tfs
     */
    public int produitVectorielDocMotsIdf(ArrayList<String> mots, Documents docModel, String doc_name, int nbDocuments) {
        double result = 0.;
        for (String mot : mots) {
            int id_document = docModel.getId(doc_name);
            double nbDocs = (double) nbDocs(mot);
            double idf = 1;
            if (nbDocs > 0) {
                idf = log((double) nbDocuments / nbDocs);
            }
            result += idf * getTf(id_document, mot);
        }
        return (int) Math.round(result);
    }

    /**
     * On calcule le nombre de documents où le mot apparait
     *
     * @param mot
     * @return nombre de documents
     */
    public int nbDocs(String mot) {
        int result = 0;
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM tf WHERE mot LIKE '" + mot + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result++;
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Mots.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
        return result;
    }

    /**
     * Pour un document donné, renvoie la somme de tous les tf des mots contenus
     * dans ce document
     *
     * @param doc_name nom du document
     * @param docModel modele de la table documents
     * @return somme des tfs
     */
    public int getTfDoc(String doc_name, Documents docModel) {
        int result = 0;
        int id_document = docModel.getId(doc_name);
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM tf WHERE id_document LIKE " + id_document;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result += rs.getInt("tf");
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Mots.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
        return result;
    }

    /**
     * Truncate the table
     */
    public void truncate() {
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "TRUNCATE tf";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Tf.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
    }
}
