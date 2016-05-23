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
      double[] attributeRuleErrors = new double[trainingSet[0].length];
      Arrays.fill(attributeRuleErrors, 1.0);
      for (int j = 0; j < trainingSet[0].length; j++) { 
         if (j == classIdx) {
            rules.add(null);
            continue;
         }
         currentRule = new TreeMap<String, String>();
         rules.add(currentRule);
         for (int i = 0; i < trainingSet.length; i++) {
            
         }
      }
      rule = rules.get(ruleAttributeIdx);
   }
   
   public String classify(String[] instance) {
      return rule.get(instance[ruleAttributeIdx]);
   }
}
