package ch.hsrmarket.android.api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class GsonClient {
    private static Gson gson = null;

    public static Gson getClient() {
        if (gson==null) {
            gson = new Gson();
        }
        return gson;
    }
}
