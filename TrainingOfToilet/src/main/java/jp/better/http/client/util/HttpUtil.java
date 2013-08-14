package jp.better.http.client.util;

import org.hk.training.toilet.util.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public final class HttpUtil {

    private HttpUtil() {
        throw new UnsupportedOperationException();
    }

    public static String getRequestParam(final Map<String, String> parameter, final String encode)
            throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        if (parameter != null && parameter.size() > 0) {
            for (Map.Entry<String, String> param : parameter.entrySet()) {
                if (param.getValue().length() != 0) {
                    builder.append(param.getKey());
                    builder.append("=");
                    builder.append(URLEncoder.encode(param.getValue(), encode));
                    builder.append("&");
                }
            }
        }
        String parameters = builder.toString();
        if (parameters != null && parameters.length() != 0) {
            parameters = builder.toString().substring(0, builder.length() - 1);
        }
        Util.log(parameters);
        return parameters;
    }

    public static String createPostParam(final String key, final String value) {
        StringBuilder builder = new StringBuilder(0);
        builder.append(key);
        builder.append("=");
        builder.append(value);
        builder.append("\r\n");
        return builder.toString();
    }

    public static String createMultiPart(final String key, final String value,
            final String boundary) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(0);
        builder.append("--");
        builder.append(boundary);
        builder.append("\r\n");
        builder.append("Content-Disposition: form-data; name=\"");
        builder.append(key);
        builder.append("\"\r\n\r\n");
        builder.append(value);
        builder.append("\r\n");
        return builder.toString();
    }

    public static String createMultiPartFile(final String key, final String boundary,
            final String fileName, final String contentType) {
        StringBuilder builder = new StringBuilder(0);
        builder.append("--");
        builder.append(boundary);
        builder.append("\r\n");
        builder.append("Content-Disposition: form-data; name=\"");
        builder.append(key);
        builder.append("\"; filename=\"");
        builder.append(fileName);
        builder.append("\"\r\n");
        builder.append("Content-Type: ");
        builder.append(contentType);
        builder.append("\r\n\r\n");
        return builder.toString();
    }
}
