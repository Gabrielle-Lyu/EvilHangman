import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvilSolution {
    private ArrayList<String> targetList;
    private final ArrayList<Character> partialSolution;
    private int missingChars;

    public EvilSolution(ArrayList<String> target){
        this.targetList = target;
        int wordLength = targetList.get(0).length();

        missingChars = wordLength;
        this.partialSolution = new ArrayList<>(missingChars);

        for(int i = 0; i < wordLength; i++) {
            this.partialSolution.add('_');
        }
    }

    public boolean isSolved(){
        return missingChars == 0;
    }

    public String getFinalSolution(){
        if (isSolved()){
            StringBuilder builder = new StringBuilder();
            for (Character c : partialSolution){
                builder.append(c);
            }
            return builder.toString();
        }
        return null;
    }

    public void printProgress() {
        for (char c : partialSolution) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    public boolean addGuess(char guess){
        boolean guessCorrect = false;
        Map<String,ArrayList<String>> currentMap = listToMap(targetList,guess);
        String currentKey = currentMap.keySet().iterator().next();
        targetList = currentMap.get(currentKey);

        for (int i = 0; i < currentKey.length(); i++) {
            if (currentKey.charAt(i) == guess) {
                partialSolution.set(i, guess);
                missingChars--;
                guessCorrect = true;
            }
        }
        return guessCorrect;
    }

    private Map<String,ArrayList<String>> listToMap(ArrayList<String> wordList, char guess){

//        Put everything in the arraylist to a map based on the pattern
        Map<String,ArrayList<String>> wordMap = new HashMap<>();

        for (String word : wordList) {
            String pattern = createPattern(word, guess);

            wordMap.putIfAbsent(pattern, new ArrayList<>());
            wordMap.get(pattern).add(word);
        }

//        Only keep the pair of the pattern and the longest list
        String longestPattern = null;
        int maxLength = 0;

        for (Map.Entry<String, ArrayList<String>> entry : wordMap.entrySet()) {
            int currentListSize = entry.getValue().size();
            if (currentListSize > maxLength) {
                maxLength = currentListSize;
                longestPattern = entry.getKey();
            }
        }

        Map<String, ArrayList<String>> filteredMap = new HashMap<>();
        if (longestPattern != null) {
            filteredMap.put(longestPattern, wordMap.get(longestPattern));
        }

        return filteredMap;
    }

    private static String createPattern(String word, char guess) {
        StringBuilder pattern = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                pattern.append(guess);
            } else {
                pattern.append('_');
            }
        }

        return pattern.toString();
    }
}
