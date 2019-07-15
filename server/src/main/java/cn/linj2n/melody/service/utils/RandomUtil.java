package cn.linj2n.melody.service.utils;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int RESET_CODE_DIGIT_COUNT = 20;

    private static final int PROFILE_VERIFICATION_CODE_DIGIT_COUNT = 6;


    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(RESET_CODE_DIGIT_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(RESET_CODE_DIGIT_COUNT);
    }

    /**
     * Generates a reset code.
     *
     * @return the generated reset code
     */
    public static String generateResetCode() {
        return RandomStringUtils.randomNumeric(RESET_CODE_DIGIT_COUNT);
    }

    public static String generateVerificationCode() {
        return RandomStringUtils.randomNumeric(PROFILE_VERIFICATION_CODE_DIGIT_COUNT);
    }

}
