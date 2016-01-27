package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Evaluator {

    private static final String QRELS_FOLDER = "qrels";

    /**
     * Parse le fichier resultat de la requete selectionnée
     * @param qrelId
     * @return 
     */
    private HashMap<String, Double> parseQrel(Integer qrelId) {
        HashMap<String, Double> result = new HashMap<>();
        try {
            InputStream ins = new FileInputStream(QRELS_FOLDER + "/qrelQ" + qrelId + ".txt");
            InputStreamReader insr = new InputStreamReader(ins, "ISO-8859-1");
            BufferedReader br = new BufferedReader(insr);
            String ligne;

            while ((ligne = br.readLine()) != null) {
                String aux[] = ligne.split("	");
                aux[1] = aux[1].replace(',', '.');
                result.put(aux[0], Double.parseDouble(aux[1]));
            }
            br.close();
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(Evaluator.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Evalue la performance des resultats obtenus pour une requete
     * @param results
     * @param qrelId 
     */
    public void evaluate(HashMap<String, Integer> results, Integer qrelId) {
        int i = 0;
        Double p5 = 0., p10 = 0., p25 = 0.;
        Double nbDocPertSelectionnes = 0.;
        Double nbDocPertTotal = 0.;
        Double nbDocTotalSelectionnes = 0.;
        HashMap<String, Double> reference = parseQrel(qrelId);
        TreeMap<String, Integer> treeMap = new TreeMap<>(results);
        for (Map.Entry<String, Double> entry : reference.entrySet()) {
            if (entry.getValue() > 0) {
                nbDocPertTotal++;
            }
        }
        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            i++;
            if (entry.getValue() > 0) {
                nbDocTotalSelectionnes++;
                if (reference.get(entry.getKey()) != null) {
                    if (reference.get(entry.getKey()) > 0) {
                        nbDocPertSelectionnes++;
                    }
                }
            }
            if (i == 5) {
                p5 = nbDocPertSelectionnes;
            } else if (i == 10) {
                p10 = nbDocPertSelectionnes;
            } else if (i == 25) {
                p25 = nbDocPertSelectionnes;
            }
        }
        p5 /= nbDocTotalSelectionnes;
        p10 /= nbDocTotalSelectionnes;
        p25 /= nbDocTotalSelectionnes;
        Double recall = nbDocPertSelectionnes / nbDocPertTotal;
        Double precision = nbDocPertSelectionnes / nbDocTotalSelectionnes;
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        System.out.println("Pertinence du résultat de la requête : ");
        System.out.println("   Recall : " + numberFormat.format(recall * 100) + "%");
        System.out.println("   Precision : " + numberFormat.format(precision * 100) + "%");
        System.out.println("   P@5 : " + numberFormat.format(p5 * 100) + "%");
        System.out.println("   P@10 : " + numberFormat.format(p10 * 100) + "%");
        System.out.println("   P@25 : " + numberFormat.format(p25 * 100) + "%");
    }
}
