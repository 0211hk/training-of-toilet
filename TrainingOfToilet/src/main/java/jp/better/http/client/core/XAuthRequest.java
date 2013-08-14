package jp.better.http.client.core;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jp.better.http.client.XAuthConfig;
import jp.better.http.client.agency.RequestParam;
import jp.better.http.client.util.Base64;
import jp.better.http.client.util.HttpUtil;
import jp.better.http.client.util.SignatureEncode;

public class XAuthRequest<T> extends AbstractRequest<T> {

    private final XAuthConfig config;
    final String SIGN_FORMAT = "%s&%s";
    private final RequestParam.OAuthRequestParam<T> requestParam;
    private Map<String, String> postParameter = new HashMap<String, String>();
    private SortedMap<String, String> authParameter = new TreeMap<String, String>();

    public XAuthRequest(final RequestParam.OAuthRequestParam<T> requestParam) {
        super(requestParam);
        this.requestParam = requestParam;
        this.config = this.requestParam.getConfig();
    }

    @Override
    public T request() throws InvalidKeyException, NoSuchAlgorithmException, IOException,
            JSONException {
        postParameter = requestParam.getParameters();
        authParameter = requestParam.getAuthParameter();

        String url = this.requestParam.getUrl();
        if (this.requestParam.getMethod() == RequestParam.HttpMethod.GET) {
            if (postParameter != null && !postParameter.isEmpty()) {
                String u = this.requestParam.getUrl().indexOf("?") != -1 ? "&" : "?";
                url = this.requestParam.getUrl() + u + HttpUtil.getRequestParam(postParameter, requestParam.getRequestEncode());
            }
        }
        HttpConnect c = new HttpConnect();

        Response r = c.connect(url, this, this.requestParam.getResponseEncode());
        return requestParam.generateResponse(r);
    }

    private String createAuthorizationValue() throws InvalidKeyException, NoSuchAlgorithmException,
            UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("OAuth ");
        for (Map.Entry<String, String> param : authParameter.entrySet()) {
            builder.append(param.getKey()).append("=");
            builder.append("\"").append(param.getValue()).append("\",");
        }
        builder.append("oauth_signature=");
        String sig = getSignature();
        builder.append("\"").append(sig).append("\"");
        return builder.toString();
    }

    private String getSignature() throws NoSuchAlgorithmException, InvalidKeyException,
            UnsupportedEncodingException {
        String keyString = String.format(
                SIGN_FORMAT,
                config.getConsumerSecret(),
                config.getAccessTokenSercret());
        String signatureBaseString = getSignatureBaseString();
        Mac mac = Mac.getInstance(this.config.getAlgolithm());
        Key key = new SecretKeySpec(keyString.getBytes(), this.config.getAlgolithm());
        mac.init(key);
        byte[] digest = mac.doFinal(signatureBaseString.getBytes());
        return encodeURL(Base64.encodeBytes(digest));
    }

    private String encodeURL(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length() && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    private String getSignatureBaseString() throws UnsupportedEncodingException {
        return requestParam.getMethod().toString() + "&" + encodeURL(requestParam.getUrl()) + "&"
                + SignatureEncode.encode(getRequestParameters());
    }

    private String getRequestParameters() throws UnsupportedEncodingException {
        SortedMap<String, String> map = authParameter;
        if (postParameter != null && postParameter.size() > 0) {
            for (Map.Entry<String, String> param : postParameter.entrySet()) {
                map.put(param.getKey(), encodeURL(param.getValue()));
            }
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> param : map.entrySet()) {
            builder.append(param.getKey());
            builder.append("=");
            builder.append(param.getValue());
            builder.append("&");
        }
        String parameters = builder.toString();
        if (parameters != null && parameters.length() != 0) {
            parameters = builder.toString().substring(0, builder.length() - 1);
        }
        return parameters;
    }

    @Override
    public void setProperties(HttpURLConnection urlConnection) throws InvalidKeyException,
            NoSuchAlgorithmException, UnsupportedEncodingException {
        if (requestParam.getProperties() != null && !requestParam.getProperties().isEmpty()) {
            for (Map.Entry<String, String> param : requestParam.getProperties().entrySet()) {
                if (param.getValue().length() != 0) {
                    urlConnection.setRequestProperty(param.getKey(), param.getValue());
                }
            }
        }
        if (isDoOutput()) {
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                    + AbstractRequest.BOUNDARY);
        }
        urlConnection.setRequestProperty("Authorization", createAuthorizationValue());
    }
}
