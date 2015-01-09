package se.alkohest.irkksome.model.api;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by oed on 2014-11-12.
 */
public class KeyPairHelper {

    public static final String GEN_ALGORITHM = "DSA";
    public static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final int KEY_SIZE = 1024;
    // TODO - select these more carefully

    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final String PRIVATE_KEY_FILE = "private.key";

    private final Context context;

    public KeyPairHelper(Context context) {
        this.context = context;
    }

    public KeyPair getKeyPair() throws IOException {
        KeyPair keyPair;
        try {
            keyPair = loadKeyPair();
        } catch (FileNotFoundException e) {
            keyPair = generateKeyPair();
        }
        return keyPair;
    }

    private KeyPair generateKeyPair() throws IOException {
        KeyPair kp = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(GEN_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            keyGen.initialize(KEY_SIZE, random);
            kp = keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        saveKeyPair(kp);
        return kp;
    }

    private KeyPair loadKeyPair() throws IOException {
        KeyPair kp = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(GEN_ALGORITHM);

            byte[] pubString = readFromFile(PUBLIC_KEY_FILE);
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubString);
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(readFromFile(PRIVATE_KEY_FILE));

            kp = new KeyPair(keyFactory.generatePublic(pubKeySpec),
                            keyFactory.generatePrivate(privKeySpec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return kp;
    }

    private void saveKeyPair(KeyPair keyPair) throws IOException {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
        writeToFile(PUBLIC_KEY_FILE, pubKeySpec.getEncoded());

        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
        writeToFile(PRIVATE_KEY_FILE, privKeySpec.getEncoded());
    }

    private void writeToFile(String file, byte[] data) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
        outputStream.write(data);
        outputStream.close();
    }

    private byte[] readFromFile(String file) throws IOException {
        FileInputStream inputStream = context.openFileInput(file);
        int size = (int) context.getFileStreamPath(file).length();
        byte[] bytes = new byte[size];
        inputStream.read(bytes);
        inputStream.close();

        return bytes;
    }
}
