package jp.better.http.client;

import android.util.Base64;

public final class StdConfig implements Config {

    private StdConfig() {
    }

    private String userName = "";
    private String password = "";

    static StdConfig getInstance(final String userName, final String password) {
        StdConfig c = new StdConfig();
        c.userName = userName;
        c.password = password;
        return c;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean has() {
        return userName.length() != 0 && password.length() != 0;
    }

    public String toBase64() {
        String auth = String.format("%s:%s", userName, password);
        return Base64.encodeToString(auth.getBytes(), Base64.DEFAULT);
    }
}
