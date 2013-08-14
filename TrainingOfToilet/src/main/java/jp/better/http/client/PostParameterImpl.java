package jp.better.http.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.better.http.client.agency.RequestParam.HttpMethod;

class PostParameterImpl implements PostParameter {

    private final HttpMethod method;
    private final String urls;
    private final boolean isMultipart;
    private final boolean isDirectPostMessage;
    private final String directPostMessage;
    private final String responseEncode;
    private final String requestEncode;
    private Map<String, String> paramMap = new ConcurrentHashMap<String, String>();
    private Map<String, String> headMap = new ConcurrentHashMap<String, String>();
    private Map<String, String> attatchMap = new ConcurrentHashMap<String, String>();

    public PostParameterImpl(final HttpMethod method, final String urls, final boolean isMultipart,
                             final boolean isDirectPostMessage, final String directPostMessage, final String responseEncode, final String requestEncode) {
        if (method == null) {
            throw new NullPointerException("HttpMethod is null.Must be set HttpMethod.");
        }
        if (urls == null || urls.equals("")) {
            throw new NullPointerException("Url is null.Must be set Url.");
        }
        this.isMultipart = isMultipart;
        this.isDirectPostMessage = isDirectPostMessage;
        this.directPostMessage = directPostMessage;
        this.method = method;
        this.urls = urls;
        this.responseEncode = responseEncode;
        this.requestEncode = requestEncode;
    }

    public PostParameterImpl setParamMap(final Map<String, String> paramMap) {
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                this.paramMap.put(key, paramMap.get(key));
            }
        }
        return this;
    }

    public PostParameterImpl setHeadMap(final Map<String, String> propMap) {
        if (propMap != null) {
            for (String key : propMap.keySet()) {
                this.headMap.put(key, propMap.get(key));
            }
        }
        return this;
    }

    public PostParameterImpl setAttatchMap(final Map<String, String> fileMap) {
        if (fileMap != null) {
            for (String key : fileMap.keySet()) {
                this.attatchMap.put(key, fileMap.get(key));
            }
        }
        return this;
    }

    @Override
    public void setParameters(Map<String, String> defaultParameter) {
        if (paramMap != null && !paramMap.isEmpty()) {
            for (String key : paramMap.keySet()) {
                defaultParameter.put(key, paramMap.get(key));
            }
            //paramMap.clear();
        }
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return urls;
    }

    @Override
    public void setProperties(Map<String, String> properites) {
        if (headMap != null && !headMap.isEmpty()) {
            for (String key : headMap.keySet()) {
                properites.put(key, headMap.get(key));
            }
            headMap.clear();
        }
    }

    @Override
    public void setFiles(Map<String, String> files) {
        if (attatchMap != null && !attatchMap.isEmpty()) {
            for (String key : attatchMap.keySet()) {
                files.put(key, attatchMap.get(key));
            }
            //attatchMap.clear();
        }
    }

    @Override
    public PostParameterImpl clone() {
        PostParameterImpl dest = new PostParameterImpl(
                this.method,
                this.urls,
                this.isMultipart,
                this.isDirectPostMessage,
                this.directPostMessage,
                this.responseEncode,
                this.requestEncode);
        dest.setAttatchMap(this.attatchMap);
        dest.setParamMap(this.paramMap);
        dest.setHeadMap(this.headMap);
        return dest;
    }

    @Override
    public String getResponseEncode() {
        return responseEncode;
    }

    @Override
    public String getRequestEncode() {
        return requestEncode;
    }

    @Override
    public String getDirectPostMessage() {
        return directPostMessage;
    }

    @Override
    public boolean isMultipart() {
        return isMultipart;
    }

    @Override
    public boolean isDirectWrite() {
        return isDirectPostMessage;
    }
}
