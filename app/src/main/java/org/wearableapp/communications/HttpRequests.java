package org.wearableapp.communications;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public abstract class HttpRequests {

    /**
     * @param params Params to send request
     * @return request response
     * @throws URISyntaxException createURI process
     */
    public static HttpResponse doPost(List params, String path) throws URISyntaxException {
        URI url = URIUtils.createURI(Server.SCHEME_HTTP, Server.HOST, Server.PORT_HTTP,
                path, null, null);
        HttpPost post = new HttpPost(url);

        if (params != null && !params.isEmpty()) {
            try {
                post.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        DefaultHttpClient hc = new DefaultHttpClient();
        HttpResponse response = null;

        try {
            response = hc.execute(post);
        } catch (HttpHostConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}