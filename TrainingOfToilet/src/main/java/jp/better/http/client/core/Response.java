package jp.better.http.client.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Response {

    private final int statusCode;
    private final String body;
    private final Map<String, List<String>> header;
    private final InputStream inputStream;

    Response(final int statusCode, final String body, final Map<String, List<String>> header,
            final InputStream inputStream) {
        this.statusCode = statusCode;
        this.body = body;
        this.header = header;
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }
}
