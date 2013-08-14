package jp.better.http.client.agency;

import org.json.JSONArray;
import org.json.JSONException;

import jp.better.http.client.Config;
import jp.better.http.client.PostParameter;
import jp.better.http.client.core.Response;

public class JsonArray extends AbstractRequestParam<JsonArray.Return> {

    public JsonArray(final PostParameter postParameter, final Config config) {
        super(postParameter, config);
    }

    @Override
    public JsonArray.Return generateResponse(Response response) {
        try {
            return new Return(new JSONArray(response.getBody()), response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public class Return implements RequestParam.Return<JSONArray> {
        private final JSONArray jsonArray;
        private final Response response;

        Return(final JSONArray jsonArray, final Response response) {
            this.jsonArray = jsonArray;
            this.response = response;
        }

        @Override
        public Response getResponse() {
            return response;
        }

        @Override
        public JSONArray getTypeResult() {
            return jsonArray;
        }
    }
}
