package classify;

public interface Classifier {
   public void train(String[][] trainingSet, int classIdx);
   public String classify(String[] instance);
}
