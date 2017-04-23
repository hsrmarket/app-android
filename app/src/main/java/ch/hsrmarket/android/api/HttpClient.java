package ch.hsrmarket.android.api;

import okhttp3.OkHttpClient;

public class HttpClient {
    private static OkHttpClient httpClient = null;

    public static OkHttpClient getClient() {
        if (httpClient==null) {
            httpClient = new OkHttpClient();
        }
        return httpClient;
    }
}
