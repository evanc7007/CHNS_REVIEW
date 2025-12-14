import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class reader {
    private static final Scanner SCANNER = new Scanner(System.in);

    private static int index = -1;

    private static List<String> valuesList;

    private static boolean got = false;

    // helpful methods
    public static int getSize(Map<String, String> map) {
        return map.size();
    }

    public static String getQuestion(Map<String, String> map) {
        // gets all the quotes
        if(!got){
            valuesList = new ArrayList<>(map.keySet());
            got = true;
        }
        // chooses a random number
        //int index = (int) (getSize(map) * Math.random());
        // gets the corresponding quote
        if(index >= getSize(map) || index == -1){
            index = 0;
            Collections.shuffle(valuesList);
        }
        String randQuote = valuesList.get(index++);
        return randQuote;
    }

    public static String getCorrectAnswer(Map<String, String> map, String randQuote) {
        // gets the corresponding author and work
        return map.get(randQuote);
    }

    public static Boolean checkAnswer(String userAnswer, String correctAnswer) {
            return userAnswer.equals(correctAnswer);
        }

    public static Map<String, String> getMap() {
        return quotesMap;
    }

    public static void main(String[] args) {
        System.out.println("Enter the file name:");
        String fileName = SCANNER.nextLine(); // <- do NOT close System.in
        load(fileName);
    }

    private static Map<String, String> quotesMap = new HashMap<>();

    public static void load(String fileName) {
        index = -1;
        got = false;
        quotesMap.clear();
        Path f = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(f)) {
            String line;
            while ((line = br.readLine()) != null) {
                /*
                 * To me, the easiest way we split the txt file line is by the middle
                 * // then everything before is key, everything after is value
                 * // make sure the txt file is formatted correctly
                 * // i.e. put quotation marks around the quote
                 */
                String[] parts = line.split("@", 2);
                // This is specific to my .txt file.
    // Found the .split method online, the 2 means it will only split into 2 parts
                if (parts.length == 2) {
                    // .trim just removes any trailing and leading spaces
                    String quote = parts[0].trim();
                    String authorAndWork = parts[1].trim();
                    quotesMap.put(quote, authorAndWork);
                }
            }
        } catch (Exception e) {
            // Silently fail - let GUI handle the error
        }
    }
}