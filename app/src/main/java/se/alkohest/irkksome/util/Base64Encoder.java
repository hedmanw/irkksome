package se.alkohest.irkksome.util;

import android.util.Base64;

public class Base64Encoder {
    public static String createPubkey(byte[] key) {
//        return "ssh-dss " + Base64.encodeToString(key, Base64.NO_WRAP) + " irkksome";
        return "ssh-rsa " + Base64.encodeToString(key, Base64.NO_WRAP) + " irkksome";
    }

    public static String createPrivkey(byte[] key) {

        return "-----BEGIN CERTIFICATE-----\n" +
                "-----BEGIN RSA PRIVATE KEY-----\n" +
                Base64.encodeToString(key, Base64.DEFAULT) +
                "\n-----END RSA PRIVATE KEY-----" +
                "\n-----END CERTIFICATE-----";
    }
}
