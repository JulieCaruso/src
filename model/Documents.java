/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kapouter
 */
public class Documents {

    private static int nextId;
    private Connection conn;

    public Documents(Connection connection) {
        this.nextId = 1;
        this.conn = connection;
    }
    
    public int getId(String name) {
        int id = 0;
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM documents WHERE name_document LIKE '" + name + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt("id_document");
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

    public void insert(String doc_name) {
        Statement stmt = null;
        String sql = null;
        try {
            stmt = conn.createStatement();
            sql = "INSERT INTO documents (name_document) VALUES ('" + doc_name + "')";;
            stmt.execute(sql);
            stmt.close();    
        } catch (SQLException ex) {
            Logger.getLogger(Documents.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Documents.nextId++;
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
        }
    }
}
