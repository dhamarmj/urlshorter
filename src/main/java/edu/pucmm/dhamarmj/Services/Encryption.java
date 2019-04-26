package edu.pucmm.dhamarmj.Services;

import sun.misc.*;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    static final String vector = "8547963214569874"; // This has to be 16 characters
    static final String secretKey= "practicafinal47";

    private static String md5(final String input) throws NoSuchAlgorithmException {

        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] messageDigest = md.digest(input.getBytes());
        final BigInteger number = new BigInteger(1, messageDigest);
        return String.format("%032x", number);

    }

    private static Cipher initCipher(final int mode, final String initialVectorString, final String secretKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        final SecretKeySpec skeySpec = new SecretKeySpec(md5(secretKey).getBytes(), "AES");
        final IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
        final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(mode, skeySpec, initialVector);
        return cipher;
    }

    public static String Encrypt(final String dataToEncrypt) {

        String encryptedData = null;

        try {
            final Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, vector, secretKey);
            final byte[] encryptedByteArray = cipher.doFinal(dataToEncrypt.getBytes());
            encryptedData = (new BASE64Encoder()).encode(encryptedByteArray);
        } catch (Exception e) {
            System.err.println("Problem encrypting the data");
            e.printStackTrace();
        }
        return encryptedData;
    }

    public static String Decrypt(final String encryptedData) {
        String decryptedData = null;
        try {
            final Cipher cipher = initCipher(Cipher.DECRYPT_MODE, vector, secretKey);
            final byte[] encryptedByteArray = (new BASE64Decoder()).decodeBuffer(encryptedData);
            final byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
            decryptedData = new String(decryptedByteArray, "UTF8");
        } catch (Exception e) {
            System.err.println("Problem decrypting the data");
            e.printStackTrace();
        }
        return decryptedData;
    }

}
