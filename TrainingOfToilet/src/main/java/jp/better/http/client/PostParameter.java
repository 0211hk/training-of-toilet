package jp.better.http.client;

import java.util.Map;

import jp.better.http.client.agency.RequestParam.HttpMethod;

public interface PostParameter {

    void setParameters(Map<String, String> defaultParameter);

    HttpMethod getMethod();

    String getUrl();

    void setProperties(Map<String, String> properites);

    void setFiles(Map<String, String> files);

    String getDirectPostMessage();

    boolean isMultipart();

    boolean isDirectWrite();

    PostParameterImpl clone();

    String getResponseEncode();

    String getRequestEncode();
}
