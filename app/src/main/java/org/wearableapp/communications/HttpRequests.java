package org.wearableapp.communications;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class HttpRequests {

    private static String response;

    /**
     * @param params Params to send request
     * @param path Path request
     * @return request response
     * @throws URISyntaxException createURI process
     */
    private static HttpResponse post(List params, String path) throws URISyntaxException {
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

    /**
     * @param params Params to send request
     * @param path Path request
     * @return true to connections status 200 e false to others connections
     */
    public static boolean doPost(final List params, final String path) {
        AsyncTask<Void, Void, String> postTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                HttpResponse response = null;
                try {
                    response = post(params, path);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String responseString = null;
                if (response != null && response.getStatusLine().getStatusCode() == 200) {
                    try {
                        responseString = EntityUtils.toString(response.getEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (responseString != null) {
                    return responseString;
                }

                return "fail";
            }
        };
        postTask.execute();

        try {
            response = postTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return !response.contains("fail");
    }

    public static String getResponse() {
        return response;
    }
}