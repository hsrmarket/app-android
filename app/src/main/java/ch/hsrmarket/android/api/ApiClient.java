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
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.ElectronicDevice;
import ch.hsrmarket.android.model.OfficeSupply;
import ch.hsrmarket.android.model.Other;
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
        public void onFailure(String msg);
    }

    private interface OnJsonReady {
         Object parse(Response response);
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

    private Callback defaultCallback(final OnJsonReady onJsonReady){
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.toString());
                misfireScenario(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.d(TAG,response.toString());
                    misfireScenario(response.toString());

                }else{
                    Object parsedOne = onJsonReady.parse(response);
                    fireScenario(parsedOne);
                }
            }
        };
    }

    private void misfireScenario(final String msg){
        if(onFailureListener != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    onFailureListener.onFailure(msg);
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

    public void requestCategoryList(Article.Type category){
        String path = "/articles";

        switch (category){
            case BOOK:
                path += "/books";
                break;
            case ELECTRONIC_DEVICE:
                path += "/electronics";
                break;
            case OFFICE_SUPPLY:
                path += "/officesupplies";
                break;
            case OTHER:
                path += "/other";
                break;

            default:
                throw new AssertionError("Forgot to implement");
        }

        httpClient
                .newCall(makeRequest(path))
                .enqueue(defaultCallback(new OnJsonReady() {
                    @Override
                    public Object parse(Response response) {

                        Type listType = new TypeToken<List<Article>>(){}.getType();
                        return gson.fromJson(response.body().charStream(),listType);
                    }
                }));
    }

    public void requestSingleArticle(int id){
        httpClient
                .newCall(makeRequest("/articles/"+id))
                .enqueue(defaultCallback(new OnJsonReady() {
                    @Override
                    public Object parse(Response response) {
                        String json = "";

                        try {
                            json = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Article article = gson.fromJson(json,Article.class);
                        Class targetClass;

                        switch (article.getType()){
                            case BOOK:
                                targetClass = Book.class;
                                break;

                            case ELECTRONIC_DEVICE:
                                targetClass = ElectronicDevice.class;
                                break;

                            case OFFICE_SUPPLY:
                                targetClass = OfficeSupply.class;
                                break;

                            case OTHER:
                                targetClass = Other.class;
                                break;

                            default:
                                throw new AssertionError("Forgot to implement");
                        }

                        return gson.fromJson(json,targetClass);
                    }
                }));
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
    }


}
