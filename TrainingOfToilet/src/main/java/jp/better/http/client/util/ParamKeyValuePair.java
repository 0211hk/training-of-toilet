package jp.better.http.client.util;

public class ParamKeyValuePair implements KeyValuePair {

    private final String key;
    private final String value;

    public ParamKeyValuePair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

}
