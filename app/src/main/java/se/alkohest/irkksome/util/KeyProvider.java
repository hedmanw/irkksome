package se.alkohest.irkksome.util;

import android.util.Log;

import java.io.IOException;
import java.security.Key;

public class KeyProvider {
    public static final String TAG = "irkksomeKEY";
    private static KeyProvider instance;

    private final String pubkey;
    private final char[] privkey;

    public static void initialize(Key pubkey, Key privkey) throws IOException {
        instance = new KeyProvider(KeyEncodingUtil.encodePublicKey(pubkey), KeyEncodingUtil.encodePrivateKey(privkey));
    }

    public static boolean hasKeys() {
        return instance != null && getPubkey() != null && getPrivkey() != null;
    }

    public static String getPubkey() {
        return instance.pubkey;
    }

    public static char[] getPrivkey() {
        return instance.privkey;
    }

    private KeyProvider(String pubkey, char[] privkey) {
        this.pubkey = pubkey;
        this.privkey = privkey;
    }

    /**
     * Typically bad to call this in production.
     * No irkksome for you if you do that!
     */
    public static void printKeyPair() {
        if (hasKeys()) {
            Log.d(TAG, "Loaded keypair:");
            Log.d(TAG, getPubkey());
            Log.d(TAG, String.valueOf(getPrivkey()));
        }
        else {
            Log.d(TAG, "Tried to print keypair, but there was no keypair loaded.");
        }
    }
}
