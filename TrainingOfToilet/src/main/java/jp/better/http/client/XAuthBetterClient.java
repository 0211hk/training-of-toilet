package jp.better.http.client;

import java.lang.reflect.Constructor;

import jp.better.http.client.agency.AbstractRequestParam;
import jp.better.http.client.agency.AbstractXAuthRequestParam;
import jp.better.http.client.agency.RequestParam;
import jp.better.http.client.agency.RequestParam.OAuthRequestParam;
import jp.better.http.client.agency.XAuthJsonArray;
import jp.better.http.client.agency.XAuthJsonObject;
import jp.better.http.client.agency.XAuthPlain;
import jp.better.http.client.core.Request;
import jp.better.http.client.core.Response;
import jp.better.http.client.core.XAuthRequest;

class XAuthBetterClient implements Client {

    private static final XAuthBetterClient INSTANCE = new XAuthBetterClient();
    private XAuthConfig config = null;
    private PostParameter postParameter = null;

    static XAuthBetterClient newInstance(final PostParameter postParameter, final XAuthConfig config) {
        INSTANCE.setPostParameter(postParameter);
        INSTANCE.setConfig(config);
        return INSTANCE;
    }

    private void setConfig(final XAuthConfig config) {
        this.config = (XAuthConfig) config.clone();
    }

    private void setPostParameter(final PostParameter postParameter) {
        this.postParameter = postParameter.clone();
    }

    @Override
    public <T> T request(CreateInstance createInstance) throws Exception {
        OAuthRequestParam<T> requestParam = createInstance.newInstance(postParameter, config);
        return execRequest(new XAuthRequest<T>(requestParam));
    }

    @Override
    public <T, T1 extends RequestParam<T>> T request(T1 t) throws Exception {
        @SuppressWarnings("unchecked")
        Class<? extends OAuthRequestParam<T>> c = (Class<? extends OAuthRequestParam<T>>) t
                .getClass();
        Constructor<? extends OAuthRequestParam<T>> constructor = c.getConstructor(
                PostParameter.class, XAuthConfig.class);
        return execRequest(new XAuthRequest<T>(constructor.newInstance(this.postParameter,
                this.config)));
    }

    @Override
    public <T, T1 extends AbstractRequestParam<T>> T request(T1 t) throws Exception {
        @SuppressWarnings("unchecked")
        Class<? extends AbstractXAuthRequestParam<T>> c = (Class<? extends AbstractXAuthRequestParam<T>>) t
                .getClass();
        Constructor<? extends AbstractXAuthRequestParam<T>> constructor = c.getConstructor(
                PostParameter.class, XAuthConfig.class);
        return execRequest(new XAuthRequest<T>(constructor.newInstance(this.postParameter,
                this.config)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public XAuthJsonArray.Return getJsonArray() throws Exception {
        return execRequest(new XAuthRequest<XAuthJsonArray.Return>(new XAuthJsonArray(
                postParameter, config)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public XAuthJsonObject.Return getJsonObject() throws Exception {
        return execRequest(new XAuthRequest<XAuthJsonObject.Return>(new XAuthJsonObject(
                postParameter, config)));
    }

    @Override
    public Response get() throws Exception {
        return execRequest(new XAuthRequest<Response>(new XAuthPlain(postParameter, config)));
    }

    private <E> E execRequest(final Request<E> req) throws Exception {
        return req.request();
    }
}
