package classify;

import java.util.TreeMap;

public class NaiveBayes {
   private double prA;
   private TreeMap<String, TreeMap<String, Integer>> freqTable;
   private TreeMap<String, Integer> classFreq;
   private int numberOfInstances;
   
   public NaiveBayes(double prA) {
      this.prA = prA;
   }
   
   public NaiveBayes() {
      this(1.0);
   }
   
   void train(String[][] trainingSet, int classIdx) {
      String instanceClass;
      TreeMap<String, Integer> row;
      Integer freq;
      freqTable = new TreeMap<>();
      classFreq = new TreeMap<>();
      numberOfInstances = trainingSet.length;
      for (String[] instance : trainingSet) {
         instanceClass = instance[classIdx];
         for (int i = 0; i < instance.length; i++) {
            if (i == classIdx) continue;
            row = freqTable.get(instanceClass);
            if (row == null) {
               row = new TreeMap<>();
               freqTable.put(instanceClass, row);
            }
            freq = row.get(instance[i]);
            if (freq == null) {
               freq = new Integer(1);
            }
            row.put(instance[i], freq + 1);
            if (classFreq.containsKey(instanceClass)) {
               classFreq.put(instanceClass, classFreq.get(instanceClass)+1);
            } else {
               classFreq.put(instanceClass, 2);
            }
         }
      }
   }
   
   public String clasify(String[] instance) {
      String[] classes = new String[classFreq.size()];
      int idxOfMax = -1;
      double maxPr = Double.NEGATIVE_INFINITY;
      double tmpPr;
      classes = classFreq.keySet().toArray(classes);
      for (int i = 0; i < classes.length; i++) {
         tmpPr = probability(classes[i], instance);
         if (idxOfMax == -1 || tmpPr > maxPr) {
            idxOfMax = i;
            maxPr = tmpPr;
         }
      }
      return classes[idxOfMax];
   }
   
   private double probability(String classVal, String[] instance) {
      double pr = 1.0;
      double classValFreq = classFreq.get(classVal); 
      double prH =  classValFreq / numberOfInstances;
      double ai;
      TreeMap<String, Integer> tableRow = freqTable.get(classVal);
      for (String attribute : instance) {
         if (tableRow.containsKey(attribute)) {
            ai = tableRow.get(attribute);
         } else {
            ai = 1;
         }
         pr *= ai / classValFreq;
      }
      return pr * prH / prA;
   }
}
