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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kapouter
 */
public class Mots {

    private static int nextId;
    private Connection conn;

    public Mots(Connection connection) {
        this.nextId = 1;
        this.conn = connection;
    }

    /**
     * Renvoie l'id du mot
     * @param mot
     * @return id
     */
    public int getId(String mot) {
        int id = 0;
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM mots WHERE mot LIKE '" + mot + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt("id_mot");
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
        return id;
    }

    /**
     * Insère le mot s'il n'exste pas déjà dans la table
     * @param mot 
     */
    public void insertIfNotPresent(String mot) {
        if (!isPresent(mot)) {
            insert(mot);
        }
    }

    /**
     * Renvoie si le mot existe déjà dans la table
     * @param mot
     * @return true ou false
     */
    public boolean isPresent(String mot) {
        boolean result = false;
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM mots WHERE mot LIKE '" + mot + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = true;
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Mots.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Mots.nextId++;
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
     * Insère un mot dans la table
     * @param mot 
     */
    public void insert(String mot) {
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "INSERT INTO mots (mot) VALUES ('" + mot + "')";;
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Mots.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Mots.nextId++;
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
    }

}
