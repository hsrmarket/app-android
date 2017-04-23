package ch.hsrmarket.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;

public class GsonClient {
    private static Gson gson = null;

    public static Gson getClient() {
        if (gson==null) {
            gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        }
        return gson;
    }
}
