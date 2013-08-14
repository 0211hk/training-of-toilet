package jp.better.http.client.agency;

import jp.better.http.client.PostParameter;
import jp.better.http.client.XAuthConfig;
import jp.better.http.client.core.Response;

@SuppressWarnings("unchecked")
public class XAuthPlain extends AbstractXAuthRequestParam<Response> {

    public XAuthPlain(final PostParameter postParameter, final XAuthConfig config) {
        super(postParameter, config);
    }

    @Override
    public Response generateResponse(Response response) {
        return response;
    }

}
