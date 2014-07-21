package se.alkohest.irkksome.irc;

public class Log {
    private final String tag;

    Log(Class logOwner) {
        String className = logOwner.getName();
        tag = className.substring(className.lastIndexOf('.') + 1);
    }

    public void e(String message) {
        android.util.Log.e(tag, message);
    }

    public void i(String message) {
        android.util.Log.i(tag, message);
    }

    public static Log getInstance(Class logOwner) {
        return new Log(logOwner);
    }
}
