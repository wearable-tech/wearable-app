package org.wearableapp.communications;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.concurrent.ExecutionException;

public abstract class HttpRequests {

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
                    Log.i("ERROR", "Connection refused 1");
                    e.printStackTrace();
                }

                if (response != null && response.getStatusLine().getStatusCode() == 200) {
                    return "success";
                }
                return "fail";
            }
        };
        postTask.execute();

        try {
            return postTask.get() == "success";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }
}