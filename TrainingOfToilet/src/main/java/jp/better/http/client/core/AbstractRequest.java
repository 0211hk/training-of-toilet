package jp.better.http.client.core;

import org.hk.training.toilet.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;

import jp.better.http.client.agency.RequestParam;
import jp.better.http.client.core.HttpConnect.HttpConnectCallback;
import jp.better.http.client.util.HttpUtil;
import jp.better.http.client.util.ImageType;

abstract class AbstractRequest<T> implements Request<T>, HttpConnectCallback {

    private final RequestParam<T> request;
    protected static final String BOUNDARY = "----WebKitFormBoundaryABmtnMvrHgP7tjLG";

    public AbstractRequest(final RequestParam<T> requestParam) {
        request = requestParam;
    }

    @Override
    public void setRequestMethod(final HttpURLConnection urlConnection) throws ProtocolException {
        urlConnection.setRequestMethod(request.getMethod().name());
    }

    @Override
    public boolean isDoOutput() {
        if (request.getMethod() != RequestParam.HttpMethod.POST) {
            return false;
        }
        if (request.getParameters() != null && !request.getParameters().isEmpty()) {
            return true;
        }
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            return true;
        }
        if (request.isDirectWrite()) {
            return true;
        }
        return false;
    }

    @Override
    public void setPostParameter(final OutputStream outputStream) throws IOException {
        if (request.isMultipart()) {
            setMultipartMessage(outputStream);
        } else if (request.isDirectWrite()) {
            setDirectMessage(outputStream);
        } else {
            setMessage(outputStream);
        }

    }

    private void setMessage(final OutputStream outputStream) throws IOException {
        Map<String, String> parameters = request.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            outputStream.write(HttpUtil.getRequestParam(parameters, request.getRequestEncode()).getBytes(request.getRequestEncode()));
        }
    }

    private void setDirectMessage(final OutputStream outputStream) throws IOException {
        String message = request.getDirectPostMessage();
        Util.log(message);
        outputStream.write(message.getBytes(request.getRequestEncode()));
    }

    private void setMultipartMessage(final OutputStream outputStream) throws IOException {
        for (Map.Entry<String, String> entry : request.getParameters().entrySet()) {
            outputStream.write(HttpUtil
                .createMultiPart(entry.getKey(), entry.getValue(), BOUNDARY)
                .getBytes("UTF-8"));
        }
        for (Map.Entry<String, String> entry : request.getFiles().entrySet()) {
            File f = new File(entry.getValue());
            String contentType = ImageType.getFormat(f).getContentType();
            String fileName = f.getName();
            if (f.exists() && f.canRead()) {
                outputStream.write(HttpUtil.createMultiPartFile(
                    entry.getKey(),
                    BOUNDARY,
                    fileName,
                    contentType).getBytes(request.getRequestEncode()));
                InputStream in = new FileInputStream(f);
                byte[] data = new byte[in.available()];
                in.read(data);
                int index = 0;
                int size = 1024;
                do {
                    if ((index + size) > data.length) {
                        size = data.length - index;
                    }
                    outputStream.write(data, index, size);
                    index += size;
                } while (index < data.length);

                in.close();
                outputStream.write("\r\n".getBytes(request.getRequestEncode()));
            }
        }
        StringBuilder builder = new StringBuilder(0);
        builder.append("--");
        builder.append(BOUNDARY);
        builder.append("--");
        builder.append("\r\n");
        outputStream.write(builder.toString().getBytes(request.getRequestEncode()));
    }
}
