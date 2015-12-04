/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
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

    public void insert(String mot) {
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "INSERT INTO mots (id_mot, mot) VALUES (" + Mots.nextId + ", '" + mot + "')";;
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
