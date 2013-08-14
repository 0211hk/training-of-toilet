package jp.better.http.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.better.http.client.agency.RequestParam.HttpMethod;
import jp.better.http.client.util.KeyValuePair;

public final class BetterClientBuilder {

    private XAuthConfig oauthConfig;
    private StdConfig stdConfig;
    private final Map<String, String> paramMap = new HashMap<String, String>();
    private final Map<String, String> headMap = new HashMap<String, String>();
    private final Map<String, String> attatchMap = new HashMap<String, String>();
    private String url = null;
    private HttpMethod method = null;
    private boolean isMultipart = false;
    private boolean isDirectPostMessage = false;
    private String directPostMessage = "";
    private String responseEncode = "UTF-8";
    private String requestEncode = "UTF-8";

    private BetterClientBuilder(final String consumerKey, final String consumerSercret,
                                final String accessToken, final String accessTokenSercret) {
        oauthConfig = XAuthConfig.getInstanceWithAccessToken(
                consumerKey,
                consumerSercret,
                accessToken,
                accessTokenSercret);
    }

    private BetterClientBuilder(final String consumerKey, final String consumerSercret,
                                final boolean isXauth) {
        if (isXauth) {
            oauthConfig = XAuthConfig.getInstance(consumerKey, consumerSercret);
        } else {
            stdConfig = StdConfig.getInstance(consumerKey, consumerSercret);
        }
    }

    private BetterClientBuilder() {
    }

    public static BetterClientBuilder getInstance() {
        return new BetterClientBuilder();
    }

    public static BetterClientBuilder getInstanceWithAuth(final String consumerKey,
                                                          final String consumerSercret) {
        return new BetterClientBuilder(consumerKey, consumerSercret, true);
    }

    public static BetterClientBuilder getInstanceWithAccessToken(final String consumerKey,
                                                                 final String consumerSecret, final String accessToken, final String accessTokenSecret) {
        return new BetterClientBuilder(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    public static BetterClientBuilder getInstanceWithBasicAuth(final String userName,
                                                               final String password) {
        return new BetterClientBuilder(userName, password, false);
    }

    public BetterClientBuilder to(final String url) {
        this.url = url;
        return this;
    }

    public <T> BetterClientBuilder to(final String url, final T... args) {
        this.url = String.format(url, args);
        return this;
    }

    public BetterClientBuilder param(final String key, final String value) {
        if (value != null && !value.equals("")) {
            this.paramMap.put(key, value);
        }
        return this;
    }

    public BetterClientBuilder param(final KeyValuePair... keyValuePair) {
        for (KeyValuePair pair : keyValuePair) {
            if (pair.getValue() != null && !pair.getValue().equals("")) {
                this.paramMap.put(pair.getKey(), pair.getValue());
            }
        }
        return this;
    }

    public BetterClientBuilder param(final Map<String, String> params) {
        for (String key : params.keySet()) {
            if (params.get(key) != null && !params.get(key).equals("")) {
                this.paramMap.put(key, params.get(key));
            }
        }
        return this;
    }

    public BetterClientBuilder param(final List<KeyValuePair> keyValuePairList) {
        for (KeyValuePair pair : keyValuePairList) {
            if (pair.getValue() != null && !pair.getValue().equals("")) {
                this.paramMap.put(pair.getKey(), pair.getValue());
            }
        }
        return this;
    }

    public BetterClientBuilder attatch(final String key, final String value) {
        if (value != null && !value.equals("")) {
            this.attatchMap.put(key, value);
        }
        return this;
    }

    public BetterClientBuilder attatch(final KeyValuePair... keyValuePair) {
        for (KeyValuePair pair : keyValuePair) {
            if (pair.getValue() != null && !pair.getValue().equals("")) {
                this.attatchMap.put(pair.getKey(), pair.getValue());
            }
        }
        return this;
    }

    public BetterClientBuilder attatch(final Map<String, String> params) {
        for (String key : params.keySet()) {
            if (params.get(key) != null && !params.get(key).equals("")) {
                this.attatchMap.put(key, params.get(key));
            }
        }
        return this;
    }

    public BetterClientBuilder attatch(final List<KeyValuePair> keyValuePairList) {
        for (KeyValuePair pair : keyValuePairList) {
            if (pair.getValue() != null && !pair.getValue().equals("")) {
                this.attatchMap.put(pair.getKey(), pair.getValue());
            }
        }
        return this;
    }

    public BetterClientBuilder head(final List<KeyValuePair> keyValuePairList) {
        for (KeyValuePair pair : keyValuePairList) {
            if (pair.getValue() != null && !pair.getValue().equals("")) {
                this.headMap.put(pair.getKey(), pair.getValue());
            }
        }
        return this;
    }

    public BetterClientBuilder head(final String key, final String value) {
        if (value != null && !value.equals("")) {
            this.headMap.put(key, value);
        }
        return this;
    }

    public BetterClientBuilder head(final KeyValuePair... keyValuePair) {
        for (KeyValuePair pair : keyValuePair) {
            if (pair.getValue() != null && !pair.getValue().equals("")) {
                this.paramMap.put(pair.getKey(), pair.getValue());
            }
        }
        return this;
    }

    public BetterClientBuilder head(final Map<String, String> params) {
        for (String key : params.keySet()) {
            if (params.get(key) != null && !params.get(key).equals("")) {
                this.headMap.put(key, params.get(key));
            }
        }
        return this;
    }

    public BetterClientBuilder method(final HttpMethod method) {
        this.method = method;
        return this;
    }

    public BetterClientBuilder multipart(final boolean isMultipart) {
        this.isMultipart = isMultipart;
        return this;
    }

    public BetterClientBuilder isDirectPostMessage(final boolean isDirectPostMessage) {
        this.isDirectPostMessage = isDirectPostMessage;
        return this;
    }

    public BetterClientBuilder directPostMessage(final String directPostMessage) {
        this.directPostMessage = directPostMessage;
        return this;
    }

    public BetterClientBuilder responseEncode(final String responseEncode) {
        this.responseEncode = responseEncode;
        return this;
    }

    public BetterClientBuilder requestEncode(final String requestEncode) {
        this.requestEncode = requestEncode;
        return this;
    }

    public BetterClientBuilder clearParam() {
        this.paramMap.clear();
        this.headMap.clear();
        this.attatchMap.clear();
        this.directPostMessage = "";
        this.isDirectPostMessage = false;
        this.isMultipart = false;
        return this;
    }

    public Client setUp() {
        PostParameter postParameter = new PostParameterImpl(
                method,
                url,
                isMultipart,
                isDirectPostMessage,
                directPostMessage,
                responseEncode,
                requestEncode).setAttatchMap(attatchMap).setParamMap(paramMap).setHeadMap(headMap);
        return BetterClient.newInstance(postParameter, stdConfig);
    }

    public Client setUpAuth() {
        PostParameter postParameter = new PostParameterImpl(
                method,
                url,
                isMultipart,
                isDirectPostMessage,
                directPostMessage,
                responseEncode,
                requestEncode).setAttatchMap(attatchMap).setParamMap(paramMap).setHeadMap(headMap);
        return XAuthBetterClient.newInstance(postParameter, oauthConfig);
    }
}
