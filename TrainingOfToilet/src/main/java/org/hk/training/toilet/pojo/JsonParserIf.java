package org.hk.training.toilet.pojo;

import org.json.JSONException;

public interface JsonParserIf<T> {

    public void parse(T obj);

    public void parse(String str);

    public T toJson() throws JSONException;
}
