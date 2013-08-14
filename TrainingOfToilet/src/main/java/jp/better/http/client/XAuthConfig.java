package jp.better.http.client;

public final class XAuthConfig implements Cloneable, Config {

    private XAuthConfig() {
    }

    private final String signatureMethod = "HMAC-SHA1";
    private final String oauthVersion = "1.0";
    private final String algolithm = "HmacSHA1";
    private String consumerKey = "";
    private String consumerSecret = "";
    private String accessToken = "";
    private String accessTokenSercret = "";

    static XAuthConfig getInstance(final String consumerKey, final String consumerSercret) {
        XAuthConfig c = new XAuthConfig();
        c.consumerKey = consumerKey;
        c.consumerSecret = consumerSercret;
        return c;
    }

    static XAuthConfig getInstanceWithAccessToken(final String consumerKey,
            final String consumerSercret, final String accessToken, final String accessTokenSercret) {
        XAuthConfig c = new XAuthConfig();
        c.consumerKey = consumerKey;
        c.consumerSecret = consumerSercret;
        c.accessToken = accessToken;
        c.accessTokenSercret = accessTokenSercret;
        return c;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public String getOauthVersion() {
        return oauthVersion;
    }

    public String getAlgolithm() {
        return algolithm;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessTokenSercret(String accessTokenSercret) {
        this.accessTokenSercret = accessTokenSercret;
    }

    public String getAccessTokenSercret() {
        return accessTokenSercret;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.setLength(0);
        str.append(String.format("signatureMethod = %s \n", getSignatureMethod()));
        str.append(String.format("oauthVersionc = %s \n", getOauthVersion()));
        str.append(String.format("algolithm = %s \n", getAlgolithm()));
        str.append(String.format("consumerKey = %s \n", getConsumerKey()));
        str.append(String.format("consumerSercret = %s \n", getConsumerSecret()));
        str.append(String.format("accessToken = %s \n", getAccessToken()));
        str.append(String.format("accessTokenSercret = %s ", getAccessTokenSercret()));
        return str.toString();
    }

    @Override
    public Object clone() {
        XAuthConfig config = new XAuthConfig();
        config.setAccessToken(this.getAccessToken());
        config.setAccessTokenSercret(this.getAccessTokenSercret());
        config.setConsumerKey(this.getConsumerKey());
        config.setConsumerSecret(this.getConsumerSecret());
        return config;
    }
}