/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author bideaud
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FileParser {

    private static String CORPUS_FOLDER = "CORPUS";
    
    private ArrayList<Document> Corpus;

    public FileParser() {

    }

    public Document parseFile(String filename) {
        File input = new File("./CORPUS/" + filename);
        Document doc = new Document("");
        try {
            doc = Jsoup.parse(input, "UTF-8", "");
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return doc;
    }
}
