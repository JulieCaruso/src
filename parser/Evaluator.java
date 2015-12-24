package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Evaluator {
    private static final String QRELS_FOLDER = "qrels";
    
    private HashMap<String, Double> parseQrel(Integer qrelId) {
        HashMap<String, Double> result = new HashMap<>();
        try {
            InputStream ins = new FileInputStream(QRELS_FOLDER+"/qrelQ"+qrelId+".txt");
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
    
    public Double evaluate(HashMap<String, Integer> results, Integer qrelId){
        Integer corrects = 0;
        Integer total = results.size();
        HashMap<String, Double> reference = parseQrel(qrelId);
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            if (reference.get(entry.getKey()) != null) {
                if (evaluateResults(entry.getValue(), reference.get(entry.getKey()))) {
                    corrects++;
                }
            } else {
                total--;
            }
        }
        Double result = corrects.doubleValue() / total.doubleValue();
        return result;
    }
    
    private boolean evaluateResults(Integer result, Double qrelResult) {
        Integer qrelRes = (int) Math.round(qrelResult);
        return result.equals(qrelRes);
    }
}
