package jp.better.http.client.core;

import org.hk.training.toilet.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class HttpConnect {

    public interface HttpConnectCallback {
        public boolean isDoOutput();

        public void setRequestMethod(final HttpURLConnection urlConnection)
                throws ProtocolException;

        public void setProperties(final HttpURLConnection urlConnection)
                throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;

        public void setPostParameter(final OutputStream outputStream) throws IOException;

    }

    public Response connect(final String url, final HttpConnectCallback httpConnectCallback, final String encode)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        FakeX509TrustManager.allowAllSSL();
        Util.log(url);
        URL u = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
        httpConnectCallback.setRequestMethod(urlConnection);
        httpConnectCallback.setProperties(urlConnection);
        urlConnection.setInstanceFollowRedirects(true);
        HttpURLConnection.setFollowRedirects(true);
        if (httpConnectCallback.isDoOutput()) {
            urlConnection.setDoOutput(true);
        }
        urlConnection.connect();
        if (httpConnectCallback.isDoOutput()) {
            OutputStream os = urlConnection.getOutputStream();
            httpConnectCallback.setPostParameter(os);
            os.flush();
            os.close();
        }
        String body = "";
        if (urlConnection.getResponseCode() == 200) {
            body = getResponseBody(urlConnection.getInputStream(), encode);
        } else {
            body = getResponseBody(urlConnection.getErrorStream(), encode);
        }
        int status = urlConnection.getResponseCode();
        Util.log(String.valueOf(status));
        Util.log(body);
        Response r = new Response(
            status,
            body,
            urlConnection.getHeaderFields(),
            urlConnection.getInputStream());
        urlConnection.disconnect();
        return r;
    }

    private String getResponseBody(final InputStream in, final String encode) throws IOException {
        StringBuilder resp = new StringBuilder(0);
        if (in == null) {
            return resp.toString();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            resp.append(line);
        }
        reader.close();
        in.close();
        return resp.toString();
    }

}
