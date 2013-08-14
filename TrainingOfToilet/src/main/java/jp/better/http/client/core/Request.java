package jp.better.http.client.core;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Request<T> {

    public T request() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            JSONException;
}
