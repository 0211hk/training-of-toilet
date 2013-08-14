package jp.better.http.client;

import java.lang.reflect.Constructor;

import jp.better.http.client.agency.AbstractRequestParam;
import jp.better.http.client.agency.JsonArray;
import jp.better.http.client.agency.JsonObject;
import jp.better.http.client.agency.Plain;
import jp.better.http.client.agency.RequestParam;
import jp.better.http.client.core.Request;
import jp.better.http.client.core.Response;
import jp.better.http.client.core.StdRequest;

class BetterClient implements Client {

    private static final BetterClient INSTANCE = new BetterClient();
    private PostParameter postParameter = null;
    private StdConfig stdConfig = null;

    private BetterClient() {
    }

    static BetterClient newInstance(final PostParameter postParameter, final StdConfig config) {
        INSTANCE.setPostParameter(postParameter);
        INSTANCE.setStdConfig(config);
        return INSTANCE;
    }

    private void setPostParameter(final PostParameter postParameter) {
        this.postParameter = postParameter.clone();
    }

    private void setStdConfig(final StdConfig config) {
        this.stdConfig = config;
    }

    @Override
    public <T> T request(CreateInstance createInstance) throws Exception {
        RequestParam<T> requestParam = createInstance.newInstance(postParameter, null);
        return execRequest(new StdRequest<T>(requestParam));
    }

    @Override
    public <T, T1 extends RequestParam<T>> T request(T1 t) throws Exception {
        @SuppressWarnings("unchecked")
        Class<? extends RequestParam<T>> c = (Class<? extends RequestParam<T>>) t.getClass();
        Constructor<? extends RequestParam<T>> constructor = c.getConstructor(PostParameter.class);
        return execRequest(new StdRequest<T>(constructor.newInstance(this.postParameter)));
    }

    @Override
    public <T, T1 extends AbstractRequestParam<T>> T request(T1 t) throws Exception {
        @SuppressWarnings("unchecked")
        Class<? extends AbstractRequestParam<T>> c = (Class<? extends AbstractRequestParam<T>>) t
                .getClass();
        Constructor<? extends AbstractRequestParam<T>> constructor = c
                .getConstructor(PostParameter.class);
        return execRequest(new StdRequest<T>(constructor.newInstance(this.postParameter)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonArray.Return getJsonArray() throws Exception {
        return execRequest(new StdRequest<JsonArray.Return>(new JsonArray(postParameter, stdConfig)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonObject.Return getJsonObject() throws Exception {
        return execRequest(new StdRequest<JsonObject.Return>(new JsonObject(postParameter,
                stdConfig)));
    }

    @Override
    public Response get() throws Exception {
        return execRequest(new StdRequest<Response>(new Plain(postParameter, stdConfig)));
    }

    private <E> E execRequest(final Request<E> req) throws Exception {
        return req.request();
    }
}
