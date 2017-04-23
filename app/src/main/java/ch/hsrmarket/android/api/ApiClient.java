package ch.hsrmarket.android.api;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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


    private Callback defaultCallback(){
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.toString());

                if(onFailureListener != null){
                    onFailureListener.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.d(TAG,response.toString());

                    if(onFailureListener != null){
                        onFailureListener.onFailure();
                    }
                }else{

                    //TODO more generic
                    Type listType = new TypeToken<ArrayList<Article>>(){}.getType();
                    List<Article> articles = gson.fromJson(response.body().charStream(),listType);

                    if(onResponseListener != null){
                        onResponseListener.onDataLoaded(articles);
                    }
                }
            }
        };
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
