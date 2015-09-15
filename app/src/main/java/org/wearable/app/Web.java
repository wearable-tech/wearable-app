package org.wearable.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Web {
    private static final String SCHEME = "http";
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 4567;
    private static final String PATH = "/methodPost";

    private Web() {};

    /**
     * @param params Params to send request
     * @return request response
     * @throws URISyntaxException createURI process
     */
    public static HttpResponse doPost(List params) throws URISyntaxException {
        URI url = URIUtils.createURI(SCHEME, HOST, PORT,
                PATH, null, null);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
