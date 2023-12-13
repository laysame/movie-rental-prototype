package input;

import interfaces.DataInputInterface;

import java.util.Scanner;

public class DataInput implements DataInputInterface {
    @Override
    public int askInteger(String prompt, int minValue, int maxValue) {
        Scanner input = new Scanner(System.in);
        boolean valid = false;
        int userInput = -1; //defaulted to -1

        do {
            System.out.println(prompt);

            try {
                userInput = Integer.parseInt(input.nextLine());
                // check the range
                if ((userInput < minValue) || (userInput > maxValue)) {
                    System.out.println("Invalid value entered. Please try a number within the range specified");
                } else {
                    valid = true;
                }

            } catch (Exception e) {
                System.out.println("ERROR -- That's not an integer value. Please, try again!");
            }

        } while (!valid);
        // must be a valid number now

        return userInput;
    }

    @Override
    public String askString(String prompt) {
        Scanner input = new Scanner(System.in);
        String userInput = "";

        do {
            System.out.println(prompt);
            userInput = input.nextLine();
        } while (userInput.isEmpty());

        return userInput;
    }
}
