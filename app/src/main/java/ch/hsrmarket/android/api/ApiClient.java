package ch.hsrmarket.android.api;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ch.hsrmarket.android.R;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.Electronic;
import ch.hsrmarket.android.model.OfficeSupply;
import ch.hsrmarket.android.model.Other;
import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Purchase;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    public interface OnResponseListener{
        public void onDataLoaded(Object data, int requestCode);
    }
    public interface OnFailureListener{
        public void onFailure(String msg, int requestCode);
    }

    private interface OnJsonReady {
         Object parse(Response response);
    }

    private interface OnInternetReady{
        void httpRequest();
    }

    public static final MediaType JSON_CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = ApiClient.class.getSimpleName();
    private static final String BASE_URL = "http://rest.hsrmarket.ch:9000/api";

    private OkHttpClient httpClient = HttpClient.getClient();
    private Gson gson = GsonClient.getClient();

    private OnResponseListener onResponseListener;
    private OnFailureListener onFailureListener;

    private Context context;
    private int requestCode = -1;

    public ApiClient(Context context, int requestCode, OnResponseListener onResponseListener, OnFailureListener onFailureListener){
        this.context = context;
        this.requestCode = requestCode;
        this.onResponseListener = onResponseListener;
        this.onFailureListener = onFailureListener;
    }

    private Request makeGetRequest(String Path){
        return  new Request.Builder().url(BASE_URL+Path).build();
    }

    private Request makePostRequest(String Path, Object obj){
        String json = gson.toJson(obj,obj.getClass());
        RequestBody body = RequestBody.create(JSON_CONTENT_TYPE,json);
        return new Request.Builder().url(BASE_URL+Path).post(body).build();
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
                    Log.d(TAG,response.toString());
                    Object parsedOne = null;

                    if(onJsonReady != null) {
                        parsedOne = onJsonReady.parse(response);
                    }
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
                    onFailureListener.onFailure(msg,requestCode);
                }
            });
        }
    }

    private void fireScenario(final Object object){
        if(onResponseListener != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    onResponseListener.onDataLoaded(object,requestCode);
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

        final String finalPath = path;
        execute(new OnInternetReady() {
            @Override
            public void httpRequest() {
                httpClient
                        .newCall(makeGetRequest(finalPath))
                        .enqueue(defaultCallback(new OnJsonReady() {
                            @Override
                            public Object parse(Response response) {

                                Type listType = new TypeToken<List<Article>>(){}.getType();
                                return gson.fromJson(response.body().charStream(),listType);
                            }
                        }));
            }
        });
    }

    public void requestSingleArticle(final int id){

        execute(new OnInternetReady() {
            @Override
            public void httpRequest() {
                httpClient
                        .newCall(makeGetRequest("/articles/"+id))
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
                                        targetClass = Electronic.class;
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
        });
    }

    public void createPurchase(Article article, Account account){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        final Purchase purchase = new Purchase(0,article, account,formattedDate,false);

        execute(new OnInternetReady() {
            @Override
            public void httpRequest() {
                httpClient
                        .newCall(makePostRequest("/purchases",purchase))
                        .enqueue(defaultCallback(null));
            }
        });

    }

    public void createPerson(final Account account){
        execute(new OnInternetReady() {
            @Override
            public void httpRequest() {
                httpClient
                        .newCall(makePostRequest("/accounts", account))
                        .enqueue(defaultCallback(new OnJsonReady() {
                            @Override
                            public Object parse(Response response) {
                                return gson.fromJson(response.body().charStream(),Account.class);
                            }
                        }));
            }
        });
    }

    public void checkCredentials(String email, String password){

        final Account account = new Account(email,password);

        execute(new OnInternetReady() {
            @Override
            public void httpRequest() {
                httpClient.
                        newCall(makePostRequest("/user/login", account)).
                        enqueue(defaultCallback(new OnJsonReady() {
                            @Override
                            public Object parse(Response response) {
                                return gson.fromJson(response.body().charStream(),Account.class);
                            }
                        }));
            }
        });
    }

    private void execute(OnInternetReady onInternetReady){
        if(isOnline()){
            onInternetReady.httpRequest();
        }else {
            misfireScenario(context.getString(R.string.msg_no_internet));
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
