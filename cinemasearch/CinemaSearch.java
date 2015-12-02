/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemasearch;

import parser.FileParser;

/**
 *
 * @author Kapouter
 */
public class CinemaSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        FileParser p = new FileParser();
        p.parseCorpus();
    }
    
}
