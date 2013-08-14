package jp.better.http.client.agency;

import org.json.JSONException;
import org.json.JSONObject;

import jp.better.http.client.PostParameter;
import jp.better.http.client.XAuthConfig;
import jp.better.http.client.core.Response;

@SuppressWarnings("unchecked")
public class XAuthJsonObject extends AbstractXAuthRequestParam<XAuthJsonObject.Return> {

    public XAuthJsonObject(final PostParameter postParameter, final XAuthConfig config) {
        super(postParameter, config);
    }

    @Override
    public XAuthJsonObject.Return generateResponse(Response response) {
        try {
            return new Return(new JSONObject(response.getBody()), response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public class Return implements RequestParam.Return<JSONObject> {
        private final JSONObject jsonObject;
        private final Response response;

        Return(final JSONObject jsonObject, final Response response) {
            this.jsonObject = jsonObject;
            this.response = response;
        }

        @Override
        public Response getResponse() {
            return response;
        }

        @Override
        public JSONObject getTypeResult() {
            return jsonObject;
        }
    }
}
