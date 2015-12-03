/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author bideaud
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FileParser {

    private static final String CORPUS_FOLDER = "CORPUS";
    private static final String EMPTYWORDS_FILE = "stopliste.txt";

    private ArrayList<String> CorpusTitles;
    private ArrayList<Document> Corpus;
    private HashMap<String, String> EmptyWords;

    public FileParser() {
        this.CorpusTitles = new ArrayList<>();
        this.Corpus = new ArrayList<>();
        this.EmptyWords = new HashMap<>();
    }
    
    public ArrayList<String> getCorpusTitles() {
        return this.CorpusTitles;
    }
    
    public ArrayList<Document> getCorpus() {
        return this.Corpus;
    }
    
    public HashMap<String, String> getEmptyWords() {
        return this.EmptyWords;
    }

    private Document parseFile(String filename) {
        File input = new File(filename);
        Document doc = new Document("");
        try {
            doc = Jsoup.parse(input, "UTF-8", "");
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return doc;
    }

    public ArrayList<Document> parseCorpus() {
        File dir = new File(CORPUS_FOLDER);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                this.CorpusTitles.add(child.getName());
                this.Corpus.add(parseFile(child.getAbsolutePath()));
            }
        }
        return this.Corpus;
    }

    public HashMap<String, String> parseEmptyWords() {
        // lecture du fichier texte 
        try {
            InputStream ins = new FileInputStream(EMPTYWORDS_FILE);
            InputStreamReader insr = new InputStreamReader(ins, "ISO-8859-1");
            BufferedReader br = new BufferedReader(insr);
            String ligne;

            while ((ligne = br.readLine()) != null) {
                this.EmptyWords.put(ligne, ligne);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return this.EmptyWords;
    }
}
