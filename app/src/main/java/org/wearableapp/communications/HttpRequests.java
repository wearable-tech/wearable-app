package org.wearableapp.communications;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class HttpRequests {

    private static String mResponse;

    /**
     * @param params Params to send request
     * @param path Path request
     * @return request mResponse
     * @throws URISyntaxException createURI process
     */
    private static HttpResponse post(List params, String path) throws URISyntaxException {
        URI uri = URIUtils.createURI(Server.SCHEME_HTTP, Server.HOST, Server.PORT_HTTP,
                path, null, null);

        if (!checkConnection(uri)) {
            throw new URISyntaxException("Connection refused", "Server is not reachable");
        }

        HttpPost post = new HttpPost(uri);

        if (params != null && !params.isEmpty()) try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

    private static boolean checkConnection(URI uri) {
        try {
            URL url = uri.toURL();
            Log.i("LOGIN", "Trying to connect to: " + url);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param params Params to send request
     * @param path Path request
     * @return 0 to connections with status 200, otherwise returns an integer greater than 0
     */
    public static int doPost(final List params, final String path) {
        AsyncTask<Void, Void, String> postTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                HttpResponse response;
                try {
                    response = post(params, path);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return "connection refused";
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
            mResponse = postTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (mResponse.contains("fail")) return 1;
        if (mResponse.contains("connection refused")) return 2;

        return 0;
    }

    public static String getResponse() {
        return mResponse;
    }
}