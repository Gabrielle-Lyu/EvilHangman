import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class EvilHangman {
    private Map<Integer, ArrayList<String>> wordList;
    private ArrayList<String> targetList;
    private Scanner inputScanner;

    private HashSet<Character> previousGuesses;

    private TreeSet<Character> incorrectGuesses;

    private EvilSolution solution;

    public EvilHangman() {
        this("engDictionary.txt");
    }

    public EvilHangman(String filename){
        try {
            wordList = dictionaryToMap(filename);
        } catch (IOException e) {
            System.out.printf(
                    "Couldn't read from the file %s. Verify that you have it in the right place and try running again.",
                    filename);
            e.printStackTrace();
            System.exit(0); // stop the program--no point in trying if you don't have a dictionary
        }

        targetList = getTargetList(wordList);

        previousGuesses = new HashSet<>();
        incorrectGuesses = new TreeSet<>();
        inputScanner = new Scanner(System.in);
        solution = new EvilSolution(targetList);
    }

    public void start() {
        while (!solution.isSolved()) {
            char guess = promptForGuess();
            recordGuesses(guess);
        }
        printVictory();
    }

    private void recordGuesses(char guess){
        previousGuesses.add(guess);
        boolean isCorrect = solution.addGuess(guess);
        if (!isCorrect) {
            incorrectGuesses.add(guess);
        }
    }

    private char promptForGuess(){
        while(true){
            System.out.println("Guess a letter.");
            solution.printProgress();
            System.out.println("Incorrect guesses:\n" + incorrectGuesses.toString());
            String input = inputScanner.next();

            if (input.length() != 1){
                System.out.println("Please enter a single letter.");
            } else if(previousGuesses.contains(input.charAt(0))){
                System.out.println("You have guessed this letter");
            } else if (!Character.isLowerCase(input.charAt(0))){
                System.out.println("Invalid input. Please enter a lowercase letter.");
            }else{
                return input.charAt(0);
            }
        }
    }

    private void printVictory(){
        System.out.printf("Congrats! The word was %s%n", solution.getFinalSolution());
    }

    private static Map<Integer,ArrayList<String>> dictionaryToMap(String filename) throws IOException {
        FileInputStream fs = new FileInputStream(filename);
        Scanner scnr = new Scanner(fs);

        Map<Integer, ArrayList<String>> wordMap = new HashMap<>();

        while (scnr.hasNext()) {
            String word = scnr.next();
            wordMap.putIfAbsent(word.length(), new ArrayList<>());
            wordMap.get(word.length()).add(word);
        }

        return wordMap;
    }

    private static ArrayList<String> getTargetList(Map<Integer, ArrayList<String>> wordMap){

//        Generate a random length which <= maximum word length of the dictionary
        List<Integer> Length = new ArrayList<>(wordMap.keySet());
        int max  = Collections.max(Length);
        int randomLength = new Random().nextInt(max) + 1;

        return wordMap.get(randomLength);
    }

}
