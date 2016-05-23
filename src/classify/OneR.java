package classify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class OneR implements Classifier {
   private TreeMap<String, String> rule;
   private int ruleAttributeIdx;
   
   public void train(String[][] trainingSet, int classIdx) {
      ArrayList<TreeMap<String, String>> rules = new ArrayList<>();
      TreeMap<String, String> currentRule;
      TreeMap<String, TreeMap<String, Integer>> freqTable;
      double[] attributeRuleErrorRates = new double[trainingSet[0].length];
      Arrays.fill(attributeRuleErrorRates, 1.0);
      for (int j = 0; j < trainingSet[0].length; j++) { 
         if (j == classIdx) {
            rules.add(null);
            continue;
         }
         currentRule = new TreeMap<String, String>();
         rules.add(currentRule);
         // obtener frecuencia de cada clase para cada valor del atributo
         // actual
         freqTable = new TreeMap<>();
         for (int i = 0; i < trainingSet.length; i++) {
            String value    = trainingSet[i][j];
            String classVal = trainingSet[i][classIdx];
            TreeMap<String, Integer> row = freqTable.get(value);
            if (row == null) {
               row = new TreeMap<String, Integer>();
               freqTable.put(value, row);
            }
            Integer classFreq = row.get(classVal);
            if (classFreq == null) classFreq = 0;
            classFreq += 1;
            row.put(classVal, classFreq);
         }
         // calcular la regla y el error correspondiente
         int totalHitFreq = 0;
         for (String value : freqTable.keySet()) {
            int maxFreq = -1;
            String choosenClass = null;
            TreeMap<String, Integer> row = freqTable.get(value);
            for (String classVal : row.keySet()) {
               int currentFreq = row.get(classVal);
               if (currentFreq > maxFreq) {
                  maxFreq = currentFreq;
                  choosenClass = classVal;
               }
            }
            totalHitFreq += maxFreq;
            currentRule.put(value, choosenClass);
         }
         double numberOfInstances = trainingSet.length;
         double hitRate = totalHitFreq / numberOfInstances;
         attributeRuleErrorRates[j] = 1 - hitRate;
      }
      double minError = Double.MAX_VALUE;
      for (int i = 0; i < attributeRuleErrorRates.length; i++) {
         double errorRate = attributeRuleErrorRates[i];
         if (errorRate < minError) {
            minError = errorRate;
            ruleAttributeIdx = i;
         }
      }
      rule = rules.get(ruleAttributeIdx);
   }
   
   public String classify(String[] instance) {
      return rule.get(instance[ruleAttributeIdx]);
   }
}
