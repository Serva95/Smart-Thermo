package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ThreadLocalRandom;
import javax.xml.bind.DatatypeConverter;
import services.config.Configuration;

public class PasswordHash {
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
}
