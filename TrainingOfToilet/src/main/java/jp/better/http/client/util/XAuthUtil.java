package jp.better.http.client.util;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import jp.better.http.client.XAuthConfig;

public final class XAuthUtil {

    private XAuthUtil() {
        throw new UnsupportedOperationException();
    }

    public static SortedMap<String, String> getXAuthParameter(final XAuthConfig config) {
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("oauth_consumer_key", config.getConsumerKey());
        map.put("oauth_nonce", UUID.randomUUID().toString());
        map.put("oauth_signature_method", config.getSignatureMethod());
        map.put("oauth_timestamp", Long.toString(System.currentTimeMillis() / 1000));
        map.put("oauth_version", config.getOauthVersion());
        map.put("oauth_token", config.getAccessToken());
        return map;
    }

    public static SortedMap<String, String> getXAuthAccessParameter(final XAuthConfig config) {
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("oauth_consumer_key", config.getConsumerKey());
        map.put("oauth_nonce", UUID.randomUUID().toString());
        map.put("oauth_signature_method", config.getSignatureMethod());
        map.put("oauth_timestamp", Long.toString(System.currentTimeMillis() / 1000));
        map.put("oauth_version", config.getOauthVersion());
        return map;
    }

}
