package jp.better.http.client.core;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import jp.better.http.client.StdConfig;
import jp.better.http.client.agency.RequestParam;
import jp.better.http.client.util.HttpUtil;

public class StdRequest<T> extends AbstractRequest<T> {

    private final RequestParam<T> requestParam;
    private final StdConfig stdConfig;

    public StdRequest(final RequestParam<T> requestParam) {
        super(requestParam);
        this.requestParam = requestParam;
        stdConfig = requestParam.getConfig();
    }

    @Override
    public T request() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            JSONException {

        String url = this.requestParam.getUrl();
        if (this.requestParam.getMethod() == RequestParam.HttpMethod.GET) {
            Map<String, String> parameters = requestParam.getParameters();
            if (parameters != null && !parameters.isEmpty()) {
                String u = this.requestParam.getUrl().indexOf("?") != -1 ? "&" : "?";
                url = this.requestParam.getUrl() + u + HttpUtil.getRequestParam(parameters, requestParam.getRequestEncode());
            }
        }
        HttpConnect c = new HttpConnect();
        Response r = c.connect(url, this, this.requestParam.getResponseEncode());
        return requestParam.generateResponse(r);
    }

    @Override
    public void setProperties(final HttpURLConnection urlConnection) {
        if (requestParam.getProperties() != null && !requestParam.getProperties().isEmpty()) {
            for (Map.Entry<String, String> param : requestParam.getProperties().entrySet()) {
                if (param.getValue().length() != 0) {
                    urlConnection.setRequestProperty(param.getKey(), param.getValue());
                }
            }
        }
        if (isDoOutput() && this.requestParam.isMultipart()) {
            if (this.requestParam.isMultipart()) {
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                        + AbstractRequest.BOUNDARY);
            } else {
                urlConnection.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded");
            }
        }
        if (stdConfig != null && stdConfig.has()) {
            String base64 = stdConfig.toBase64();
            urlConnection.setRequestProperty("Authorization", "Basic " + base64);
        }
    }
}