package utils;

import java.util.Random;

public class Generator {
    public static String generateCode(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Create a StringBuilder to store the generated code
        String codeBuilder = new String();

        // Create an instance of Random
        Random random = new Random();

        // Generate random characters and append them to the codeBuilder
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            codeBuilder+=randomChar;
        }

        // Convert StringBuilder to String and return
        return codeBuilder;
    }
}
