package com.github.tool.encrypt;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * @Author: PengCheng
 * @Description:    hash PBKDF2 加密
 * @Date: 2018/4/15
 */
public class PBKDF2PasswordHasher {

    public final Integer DEFAULT_ITERATIONS = 20000;
    public final String algorithm = "pbkdf2_sha256";

    public String getEncodedHash(String password, String salt, int iterations) {
        // Returns only the last part of whole encoded password
        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Could NOT retrieve PBKDF2WithHmacSHA256 algorithm");
            System.exit(1);
        }
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(Charset.forName("UTF-8")), iterations, 256);
        SecretKey secret = null;
        try {
            secret = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            System.out.println("Could NOT generate secret key");
            e.printStackTrace();
        }

        byte[] rawHash = secret.getEncoded();
        byte[] hashBase64 = Base64.getEncoder().encode(rawHash);

        return new String(hashBase64);
    }

    /**
     * make salt
     * @return String
     */
    private static String getSalt(){
        int length = 12;
        Random rand = new Random();
        char[] rs = new char[length];
        for(int i = 0; i < length; i++){
            int t = rand.nextInt(3);
            if (t == 0) {
                rs[i] = (char)(rand.nextInt(10)+48);
            } else if (t == 1) {
                rs[i] = (char)(rand.nextInt(26)+65);
            } else {
                rs[i] = (char)(rand.nextInt(26)+97);
            }
        }
        return new String(rs);
    }

    /**
     * rand salt
     * iterations is default 20000
     * @param password
     * @return
     */
    public String encode(String password) {
        return this.encode(password, getSalt());
    }
    /**
     * rand salt
     * @param password
     * @return
     */
    public String encode(String password,int iterations) {
        return this.encode(password, getSalt(),iterations);
    }
    /**
     * iterations is default 20000
     * @param password
     * @param salt
     * @return
     */
    public String encode(String password, String salt) {
        return this.encode(password, salt, this.DEFAULT_ITERATIONS);
    }

    /**
     *
     * @param password
     * @param salt
     * @param iterations
     * @return
     */
    public String encode(String password, String salt, int iterations) {
        // returns hashed password, along with algorithm, number of iterations and salt
        String hash = getEncodedHash(password, salt, iterations);
        return String.format("%s$%d$%s$%s", algorithm, iterations, salt, hash);
    }

    public boolean checkPassword(String password, String hashedPassword) {
        // hashedPassword consist of: ALGORITHM, ITERATIONS_NUMBER, SALT and
        // HASH; parts are joined with dollar character ("$")
        String[] parts = hashedPassword.split("\\$");
        if (parts.length != 4) {
            // wrong hash format
            return false;
        }
        Integer iterations = Integer.parseInt(parts[1]);
        String salt = parts[2];
        String hash = encode(password, salt, iterations);

        return hash.equals(hashedPassword);
    }
}
