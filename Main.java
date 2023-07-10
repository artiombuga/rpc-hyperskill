package rockpaperscissors;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static boolean isActive = true;
    static List<String> gameOptions = new ArrayList<>();
    static List<String> defaultOptions = new ArrayList<>(List.of("scissors", "rock", "paper"));
    static int rating = 0;
    static String pathToFile = ".\\rating.txt";

    public static void main(String[] args) {

        greetUser();

        String userOptions = scanner.nextLine();

        if (userOptions.isEmpty()) {
            gameOptions.addAll(defaultOptions);
        } else {
            gameOptions.addAll(List.of(userOptions.split(",")));
        }
        System.out.println("Okay, let's start");

        while (isActive) {

            String userInput = scanner.next();

            if (userInput.contains("!exit")) break;
            if (userInput.equals("!rating")) {
                System.out.println(rating);
                continue;
            }
            if (!gameOptions.contains(userInput)) {
                System.out.println("Invalid input");
                continue;
            }

            String botOutput = getBotAnswer(gameOptions);

            System.out.println(checkCustomResult(userInput, botOutput));
        }

        System.out.println("Bye!");
    }

    private static void greetUser() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name);

        File ratings = new File(pathToFile);

        try (Scanner fileReader = new Scanner(ratings)) {
            while (fileReader.hasNext()) {
                if (fileReader.next().equals(name)) {
                    rating = fileReader.nextInt();
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found" + pathToFile);
        }
    }

    private static String getBotAnswer(List<String> gameOptions) {
        int randomNumber = random.nextInt(gameOptions.size());

        return gameOptions.get(randomNumber);
    }


    static String checkResult(String userInput, String botOutput) {
        String draw = "There is a draw (" + userInput + ")";
        String win = "Well done. The computer chose " + botOutput + " and failed";
        String lose = "Sorry, but the computer chose " + botOutput;

        if (userInput.equals(botOutput)) {
            rating += 50;
            return draw;
        }

        if (userInput.equals("scissors") && botOutput.equals("paper") ||
                userInput.equals("paper") && botOutput.equals("rock") ||
                userInput.equals("rock") && botOutput.equals("scissors")
        ) {
            rating += 100;
            return win;
        }

        return lose;
    }

    static String checkCustomResult(String userInput, String botOutput) {
        String draw = "There is a draw (" + userInput + ")";
        String win = "Well done. The computer chose " + botOutput + " and failed";
        String lose = "Sorry, but the computer chose " + botOutput;

        if (userInput.equals(botOutput)) {
            rating += 50;
            return draw;
        }

        if (calculateLosesAgainst(userInput, gameOptions).contains(botOutput)) {
            return lose;
        }

        rating += 100;
        return win;
    }

    static List<String> calculateLosesAgainst(String option, List<String> options) {
        List<String> optionLosesTo = new ArrayList<>();
        int optionIndex = options.indexOf(option);
        int halfSize = options.size() / 2;

        for (int i = optionIndex + 1; i < optionIndex + halfSize + 1; i++) {
            optionLosesTo.add(options.get(i % options.size()));
        }

        return optionLosesTo;
    }

}
