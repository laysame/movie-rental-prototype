package interfaces;

public interface DataInputInterface {
    /**
     * Asks the user to input a number within a range and return a integer value
     * If is not valid, keep asking
     *
     * @param prompt -- Prompt as a String
     * @return -- valid number as an integer
     */

    public int askInteger(String prompt, int minValue, int maxValue);

    /**
     * Asks the user to input a text
     *
     * @param prompt -- Prompt as a String
     * @return -- input text from the user
     */
    public String askString(String prompt);

}
