package se.alkohest.irkksome.model.api;

import android.content.Context;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import se.alkohest.irkksome.util.KeyEncodingUtil;

/**
 * Test keys can be generated with
 * $ ssh-keygen -t rsa -b 1024 -f dummy-ssh-keygen.pem -N '' -C "keyname"
 * For SSH pubkey auth:
 *  - the public key must be in SSH Public Key File Format (RFC 4716)
 *  - the private key must be PEM RSA encoded (X.509)
 */
public class KeyPairManager {

    public static final String GEN_ALGORITHM = "RSA";
    public static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final int KEY_SIZE = 1024;
    // TODO - select these more carefully

    private static final String PUBLIC_KEY_FILE = "public_rsa.key";
    private static final String PRIVATE_KEY_FILE = "private_rsa.key";

    private final Context context;

    public KeyPairManager(Context context) {
        this.context = context;
    }

    public boolean hasKeyPair() {
        boolean publicExists = false;
        boolean privateExists = false;
        final String[] fileNames = context.fileList();
        for (String fileName : fileNames) {
            if (fileName.equals(PRIVATE_KEY_FILE)) {
                privateExists = true;
            }
            if (fileName.equals(PUBLIC_KEY_FILE)) {
                publicExists = true;
            }
        }
        return publicExists && privateExists;
    }

    public KeyPair getKeyPair() throws IOException {
        KeyPair kp = null;
        if (hasKeyPair()) {
            kp = loadKeyPair();
        }
        else {
            try {
                kp = generateKeyPair();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        return kp;
    }

    private KeyPair generateKeyPair() throws IOException, NoSuchProviderException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, random);
            KeyPair kp = keyPairGenerator.generateKeyPair();
            saveKeyPair(kp);
            return kp;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
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

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        keyPairGenerator.initialize(1024, random);
        KeyPair kp = keyPairGenerator.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();

        System.out.println(KeyEncodingUtil.encodePrivateKeyToString(privateKey));
        System.out.println(KeyEncodingUtil.encodePublicKey(publicKey));
    }
}
