/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentMot {

    private Connection conn;

    public DocumentMot(Connection connection) {
        this.conn = connection;
    }

    /**
     * insère une ligne dans la table
     * @param id_document
     * @param id_mot
     * @param tf 
     */
    public void insert(int id_document, int id_mot, int tf) {
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "INSERT INTO document_mot (id_document, id_mot, tf) VALUES (" + id_document + ", '" + id_mot + "', " + tf + ")";;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DocumentMot.class.getName()).log(Level.SEVERE, null, ex);
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
     * @param id_document
     * @param id_mot
     * @return tf
     */
    public int getTf(int id_document, int id_mot) {
        int tf = 0;
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM document_mot WHERE id_document, id_mot LIKE " + id_document + ", " + id_mot;
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
     * @param mots liste des mots d'une requete
     * @param docModel modele de la table documents
     * @param motModel modele de la table mots
     * @param doc_name nom du document
     * @return somme des tfs
     */
    public int produitVectorielDocMots(ArrayList<String> mots, Documents docModel, Mots motModel, String doc_name) {
        int result = 0;
        for (String mot : mots) {
            int id_document = docModel.getId(doc_name);
            int id_mot = motModel.getId(mot);
            result += getTf(id_document, id_mot);
        }
        return result;
    }

    /**
     * Pour un document donné, renvoie la somme de tous les tf des mots contenus dans ce document
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
            sql = "SELECT * FROM document_mot WHERE id_document LIKE " + id_document;
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

}
