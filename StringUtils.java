package utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class StringUtils {
    public synchronized static String getEncodedLogin(String email, String password) {
        byte[] stringBytes = (email + ":" + password).getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(stringBytes);
    }

    public synchronized static String generateRandomStringWithNumbers(int lengthOfTheString) {
        return generateRandomString(lengthOfTheString, true, true);
    }

    public synchronized static String generateRandomStringWithOnlyNumbers(int lengthOfTheString) {
        return generateRandomString(lengthOfTheString, false, true);
    }

    public synchronized static String generateRandomString(int lengthOfTheString, boolean withLetters, boolean withNumbers) {
        return RandomStringUtils.random(lengthOfTheString, 0, 120, withLetters, withNumbers);
    }

    public synchronized static String getDataString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }
}
