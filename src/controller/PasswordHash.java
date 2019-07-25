package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import services.config.Configuration;

public class PasswordHash {

    public static String passwordHashPBKDF2(String password) {
        int iterations = 1000 * 80;
        // length of out in byte
        int byteCount = 512;
        char[] chars = password.toCharArray();
        //generate secure random number
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        String toHexSalt;
        String toHexHash;

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, byteCount);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            toHexSalt = DatatypeConverter.printHexBinary(salt);
            toHexHash = DatatypeConverter.printHexBinary(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return iterations + ":" + toHexSalt + ":" + toHexHash;
    }

    public static boolean passwordVerifyPBKDF2(String password, String storedPassword) {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        //byte[] salt = fromHex(parts[1]);
        byte[] salt = new byte[parts[1].length() / 2];
        for(int i = 0; i<salt.length ;i++){
            salt[i] = (byte)Integer.parseInt(parts[1].substring(2 * i, 2 * i + 2), 16);
        }
        byte[] testHash;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, parts[2].length()*4);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            testHash = skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return DatatypeConverter.printHexBinary(testHash).equals(parts[2]);
    }
    /*
    public static String passwordhash(String pass){
        String digconverted = null;
        String[] bgn_salts = Configuration.BEGIN_SALTS;
        String[] end_pepps = Configuration.END_PEPPERS;
        try {
            String randomSalt = bgn_salts[ThreadLocalRandom.current().nextInt(0, 9 + 1)];
            String randomPepper = end_pepps[ThreadLocalRandom.current().nextInt(0, 9 + 1)];

            MessageDigest todigest = MessageDigest.getInstance("SHA-512");
            byte[] hash = todigest.digest(pass.getBytes(StandardCharsets.UTF_8));
            digconverted = DatatypeConverter.printHexBinary(hash);

            digconverted = randomSalt + digconverted;

            hash = todigest.digest(digconverted.getBytes(StandardCharsets.UTF_8));
            digconverted = DatatypeConverter.printHexBinary(hash);

            digconverted = digconverted + randomPepper;

            hash = todigest.digest(digconverted.getBytes(StandardCharsets.UTF_8));
            digconverted = DatatypeConverter.printHexBinary(hash);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return digconverted;
    }

    public static boolean passwordVerify(String pwd, String hashed){
        boolean verified = false;
        String digconverted;
        String[] bgn_salts = Configuration.BEGIN_SALTS;
        String[] end_pepps = Configuration.END_PEPPERS;
        try{
            MessageDigest todigest = MessageDigest.getInstance("SHA-512");
            byte[] hash = todigest.digest(pwd.getBytes(StandardCharsets.UTF_8));
            String initialhash = DatatypeConverter.printHexBinary(hash);

            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    String tmp = bgn_salts[i] + initialhash;
                    hash = todigest.digest(tmp.getBytes(StandardCharsets.UTF_8));
                    digconverted = DatatypeConverter.printHexBinary(hash);
                    tmp = digconverted + end_pepps[j];
                    hash = todigest.digest(tmp.getBytes(StandardCharsets.UTF_8));
                    digconverted = DatatypeConverter.printHexBinary(hash);
                    if(digconverted.equals(hashed)){
                        j=10;
                        i=10;
                        verified = true;
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return verified;
    }
    */
}
