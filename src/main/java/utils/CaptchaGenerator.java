package utils;

import java.util.Random;

public class CaptchaGenerator {
    public static String generateCaptcha() {
        // Generate a random alphanumeric CAPTCHA
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String captcha = new String();
        Random random = new Random();

        for (int i = 0; i < 6; i++) { // Generating a 6-character long CAPTCHA
            int index = random.nextInt(allowedChars.length());
            captcha+=(allowedChars.charAt(index));
        }

        return captcha;
    }
}
