package jp.better.http.client.agency;

import java.util.Map;
import java.util.SortedMap;

import jp.better.http.client.Config;
import jp.better.http.client.core.Response;

public interface RequestParam<T> {

    public enum HttpMethod {
        GET, POST, DELETE, PUT
    }

    public interface OAuthRequestParam<T> extends RequestParam<T> {

        SortedMap<String, String> getAuthParameter();
    }

    public interface Return<M> {
        M getTypeResult();

        Response getResponse();
    }

    <E extends Config> E getConfig();

    T generateResponse(final Response response);

    Map<String, String> getParameters();

    Map<String, String> getProperties();

    Map<String, String> getFiles();

    String getDirectPostMessage();

    String getResponseEncode();

    String getRequestEncode();

    boolean isMultipart();

    boolean isDirectWrite();

    String getUrl();

    HttpMethod getMethod();

}
