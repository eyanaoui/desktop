package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Hash {
    // Generate hash for a given string using SHA-256 algorithm
    public static String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
    // Verify if the provided string matches the given hash
    public static boolean verifyHash(String input, String hash) {
        String generatedHash = generateHash(input);
        return generatedHash != null && generatedHash.equals(hash);
    }
}
