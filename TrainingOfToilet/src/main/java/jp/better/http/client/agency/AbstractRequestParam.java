package jp.better.http.client.agency;

import java.util.HashMap;
import java.util.Map;

import jp.better.http.client.Config;
import jp.better.http.client.PostParameter;
import jp.better.http.client.core.Response;

public abstract class AbstractRequestParam<T> implements RequestParam<T> {

    private final PostParameter postParameter;
    private final Config config;

    public AbstractRequestParam(final PostParameter postParameter, final Config config) {
        this.postParameter = postParameter;
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Config getConfig() {
        return this.config;
    }

    @Override
    public Map<String, String> getParameters() {
        final Map<String, String> map = new HashMap<String, String>();
        postParameter.setParameters(map);
        return map;
    }

    @Override
    public HttpMethod getMethod() {
        return postParameter.getMethod();
    }

    @Override
    public String getUrl() {
        return postParameter.getUrl();
    }

    @Override
    abstract public T generateResponse(Response response);

    @Override
    public Map<String, String> getProperties() {
        final Map<String, String> map = new HashMap<String, String>();
        postParameter.setProperties(map);
        return map;
    }

    @Override
    public Map<String, String> getFiles() {
        final Map<String, String> map = new HashMap<String, String>();
        postParameter.setFiles(map);
        return map;
    }

    @Override
    public String getDirectPostMessage() {
        return postParameter.getDirectPostMessage();
    }

    @Override
    public boolean isMultipart() {
        return postParameter.isMultipart();
    }

    @Override
    public boolean isDirectWrite() {
        return postParameter.isDirectWrite();
    }

    @Override
    public String getResponseEncode(){
        return postParameter.getResponseEncode();
    }

    @Override
    public String getRequestEncode(){
        return postParameter.getRequestEncode();
    }
}
