package jp.better.http.client.agency;

import java.util.Map;
import java.util.SortedMap;

import jp.better.http.client.PostParameter;
import jp.better.http.client.XAuthConfig;
import jp.better.http.client.agency.RequestParam.OAuthRequestParam;
import jp.better.http.client.core.Response;
import jp.better.http.client.util.XAuthUtil;

public abstract class AbstractXAuthRequestParam<T> extends AbstractRequestParam<T> implements
        OAuthRequestParam<T> {

    private final XAuthConfig config;

    public AbstractXAuthRequestParam(final PostParameter postParameter, final XAuthConfig config) {
        super(postParameter, config);
        this.config = config;
    }

    @Override
    public abstract T generateResponse(Response response);

    @Override
    public Map<String, String> getParameters() {
        return super.getParameters();
    }

    @Override
    public Map<String, String> getProperties() {
        return super.getProperties();
    }

    @Override
    public Map<String, String> getFiles() {
        return super.getFiles();
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public HttpMethod getMethod() {
        return super.getMethod();
    }

    @Override
    public SortedMap<String, String> getAuthParameter() {
        return XAuthUtil.getXAuthParameter(config);
    }

    @Override
    public XAuthConfig getConfig() {
        return config;
    }
}