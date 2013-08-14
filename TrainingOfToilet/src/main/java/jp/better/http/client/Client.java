package jp.better.http.client;

import jp.better.http.client.agency.AbstractRequestParam;
import jp.better.http.client.agency.RequestParam;
import jp.better.http.client.core.Response;

public interface Client {

    public interface CreateInstance {
        public <T extends RequestParam<?>> T newInstance(PostParameter postParameter, Config config);
    }

    public <T, T1 extends AbstractRequestParam<T>> T request(T1 t) throws Exception;

    public <T, T1 extends RequestParam<T>> T request(T1 t) throws Exception;

    public <T> T request(CreateInstance createInstance) throws Exception;

    public <T extends RequestParam.Return<T>> T getJsonArray() throws Exception;

    public <T extends RequestParam.Return<T>> T getJsonObject() throws Exception;

    public Response get() throws Exception;

}
