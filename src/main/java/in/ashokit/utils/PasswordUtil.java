package in.ashokit.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
	
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String ALL_CHARS = UPPER + LOWER;
	
	public String generateRandomPassword() {
		String alphabets = generateAlphabets(ALL_CHARS);
		String specialCharacter = generateSpecialCharacter(SPECIAL_CHARS);
		String randomTwoDigitNumber = generateRandomTwoDigitNumber();
		return alphabets + specialCharacter + randomTwoDigitNumber;
	}
	private static String generateAlphabets(String allLetters) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(5);
		for (int i = 0; i < 5; i++) {
			int index = random.nextInt(allLetters.length());
			char randomChar = allLetters.charAt(index);
			sb.append(randomChar);
		}
		return sb.toString();
	}
	private static String generateSpecialCharacter(String specialCharacters) {
		Random random = new Random();
		int randomIndex = random.nextInt(specialCharacters.length());
		char randomSpecialChar = specialCharacters.charAt(randomIndex);
		return String.valueOf(randomSpecialChar);
	}	
	private static String generateRandomTwoDigitNumber() {
		Random random = new Random();
		int randomNumber = random.nextInt(100);
		return String.format("%02d", randomNumber);
	}
}