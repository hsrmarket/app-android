package ch.hsrmarket.android.api;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ch.hsrmarket.android.model.Article;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {

    public interface OnResponseListener{
        public void onDataLoaded(Object data);
    }
    public interface OnFailureListener{
        public void onFailure();
    }

    private static final String TAG = ApiClient.class.getSimpleName();
    private static final String BASE_URL = "http://rest.hsrmarket.ch:9000/api";

    private OkHttpClient httpClient = HttpClient.getClient();
    private Gson gson = GsonClient.getClient();

    private OnResponseListener onResponseListener;
    private OnFailureListener onFailureListener;

    private Request makeRequest(String Path){
        return  new Request.Builder().url(BASE_URL+Path).build();
    }

    private <T> List<T> parseToList(Response response, TypeToken<List<T>> typeToken){
        Type listType = typeToken.getType();
        return gson.fromJson(response.body().charStream(),listType);
    }

    private Callback defaultCallback(){
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.toString());
                misfireScenario();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.d(TAG,response.toString());
                    misfireScenario();

                }else{
                    List<Article> articles = parseToList(response,new TypeToken<List<Article>>(){});
                    fireScenario(articles);
                    Log.d(TAG,articles.get(0).getName());

                }
            }
        };
    }

    private void misfireScenario(){
        if(onFailureListener != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    onFailureListener.onFailure();
                }
            });
        }
    }

    private void fireScenario(final Object object){
        if(onResponseListener != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    onResponseListener.onDataLoaded(object);
                }
            });
        }
    }

    //TODO generalize it
    public void testReq(){
        httpClient.newCall(makeRequest("/articles")).enqueue(defaultCallback());
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
    }


}
