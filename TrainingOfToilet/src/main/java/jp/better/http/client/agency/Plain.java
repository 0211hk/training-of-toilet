package jp.better.http.client.agency;

import jp.better.http.client.Config;
import jp.better.http.client.PostParameter;
import jp.better.http.client.core.Response;

public class Plain extends AbstractRequestParam<Response> {

    public Plain(final PostParameter postParameter, final Config config) {
        super(postParameter, config);
    }

    @Override
    public Response generateResponse(Response response) {
        return response;
    }

}
