
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class multipleChoice {

    private static final Scanner SCANNER = new Scanner(System.in);
    // Use one scanner for all so nothing fails.

    public static int getNumbChoices() {
        int numbChoices;
        System.out.println("Enter the number of answer choices:");
        numbChoices = SCANNER.nextInt();
        SCANNER.nextLine(); // consume the leftover newline
        return numbChoices;
    }

    //to do: question and answer split
    public static ArrayList<String> getRandAnswer(String correctAnswer, Map<String, String> map, int numbChoices) {
        ArrayList<String> randAnswers = new ArrayList<>();
        randAnswers.add(correctAnswer);
        int count = 1;
        // this is the list of all possible answers
        ArrayList<String> answerChoices = new ArrayList<>(map.values());
        // chooses a random number
        if (numbChoices > map.size()) {
            System.out.println("Error: number of choices exceeds number of available answers.");
        } else {
            while (count < numbChoices) {
                int index = (int) (map.size() * Math.random());
                String randAnswer = answerChoices.get(index);
                if (!randAnswers.contains(randAnswer)) {
                    randAnswers.add(randAnswer);
                    count++;
                }
            }
        }
        // TESTING System.out.println("Random Answers: " + randAnswers);
        return randAnswers;
    }

    public static String getUserAnswer() {
        System.out.println("Enter your answer:");
        String userAnswer = SCANNER.nextLine();
        return userAnswer;
    }

    public static String reaction(boolean isCorrect) {
        if (isCorrect) {
            return "Correct!";
        } else {
            return "Incorrect.";
        }
    }

    public static void main(String[] args) {
        int numbChoices = getNumbChoices();
        // Initialize map
        reader.main(args);
        Map<String, String> map = reader.getMap();
        // for testing System.out.println(map);

        // get quote and correct answer
        String quote = reader.getQuestion(map);
        String correctAnswer = reader.getCorrectAnswer(map, quote);

        // get random answers
        ArrayList<String> randAnswers = getRandAnswer(correctAnswer, map, numbChoices);

        // display question and answers
        System.out.println("quote: " + quote);
        System.out.println(randAnswers);

        // get user input
        String userAnswer = getUserAnswer();

        // check if correct
        //correctAnswer = correctAnswer.substring(1, correctAnswer.length() - 1);
        boolean isCorrect = reader.checkAnswer(userAnswer, correctAnswer);
        System.out.println(reaction(isCorrect));
    }
}
