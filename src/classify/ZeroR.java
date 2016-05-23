package classify;

import java.util.TreeMap;

public class ZeroR {
   private String mostFrequentClass;
   
   public void train(String[][] trainingSet, int classIdx) {
      TreeMap<String, Integer> classes = new TreeMap<>();
      int maxFreq = 0;
      for (String[] instance : trainingSet) {
         String classVal = instance[classIdx];
         Integer freq = classes.get(classVal);
         if (freq == null) {
            freq = 1;
         } else {
            freq = freq + 1;
         }
         classes.put(classVal, freq);
         if (freq > maxFreq) {
            maxFreq = freq;
            mostFrequentClass = classVal;
         }
      }
   }
   
   public String classify(String[] instance) {
      return mostFrequentClass;
   }
}
