package se.alkohest.irkksome.util;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by wilhelm 2015-02-06.
 */
public class KeyEncodingUtil {

    public static final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";

    public static String encodePublicKey(Key key) throws IOException {
        if (key instanceof RSAPublicKey) {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) key;
            final String pubkey = sshEncodePublicKey(rsaPublicKey);
            return "ssh-rsa " + pubkey + " irkksome";
        }
        throw new IOException("Key algorithm \"" + key.getAlgorithm() + "\" not recognized.");
    }

    public static String encodePrivateKeyToString(Key key) throws IOException {
        return new String(encodePrivateKey(key));
    }

    public static byte[] encodePrivateKey(Key key) throws IOException {
        if (key instanceof RSAPrivateKey) {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
            return pemEncodeKey(rsaPrivateKey, RSA_PRIVATE_KEY);
        }
        throw new IOException("Key algorithm \"" + key.getAlgorithm() + "\" not recognized.");
    }

    private static String sshEncodePublicKey(RSAPublicKey key) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] name = "ssh-rsa".getBytes();
        writeSSH(name, baos);
        writeSSH(key.getPublicExponent().toByteArray(), baos);
        writeSSH(key.getModulus().toByteArray(), baos);
        return Base64.toBase64String(baos.toByteArray());
    }

    private static void writeSSH(byte[] bytes, OutputStream outputStream) throws IOException {
        for (int shift = 24; shift >= 0; shift -= 8) {
            outputStream.write((bytes.length >>> shift) & 0xFF);
        }
        outputStream.write(bytes);
    }

    public static byte[] pemEncodeKey(Key key, String desc) throws IOException {
        final PemObject pemObject = new PemObject(desc, key.getEncoded());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(baos))) {
            pemWriter.writeObject(pemObject);
        }
        return baos.toByteArray();
    }
}
