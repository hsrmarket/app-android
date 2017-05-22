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
import java.util.ArrayList;
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

interface OnJsonReady {
    Object parse(Response response, int requestCode);
}

interface OnInternetReady{
    void httpRequest(int requestCode, Object... args);
}

public class ApiClient implements OnJsonReady, OnInternetReady {

    public interface OnResponseListener{
        public void onDataLoaded(Object data, int requestCode);
    }
    public interface OnFailureListener{
        public void onFailure(String msg, int requestCode);
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

    private Callback makeCallback(final OnJsonReady onJsonReady, final int requestCode){
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
                        parsedOne = onJsonReady.parse(response, requestCode);
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

    private void execute(OnInternetReady onInternetReady, int requestCode, Object... args){
        if(isOnline()){
            onInternetReady.httpRequest(requestCode, args);
        }else {
            misfireScenario(context.getString(R.string.msg_no_internet));
        }
    }

    private String getMyPath(int myId){
        switch (myId){
            case R.id.nav_articles:
                return "/articles";

            case R.id.nav_sales:
                return "/sales";

            case R.id.nav_purchases:
                return "/purchases";

            default:
                throw new AssertionError("Forgot to implement");
        }
    }

    private List<Article> extractArticles(List<Purchase> purchases){
        List<Article> articles = new ArrayList<>();

        for(Purchase p : purchases){
            Article a = p.getArticle();
            a.setPurchaseId(p.getId());
            articles.add(a);
        }

        return articles;
    }

    public void setRequestCode(int requestCode){
        this.requestCode = requestCode;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void getArticleList(Article.Type category){
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

        execute(ApiClient.this, GET_ARTICLE_LIST,path);
    }

    public void getArticle(int id){
        execute(ApiClient.this, GET_ARTICLE, "/articles/"+id);
    }

    public void postPurchase(Article article, Account account){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        Purchase purchase = new Purchase(0,article, account, null,formattedDate,false);

        execute(ApiClient.this,POST_PURCHASE,"/purchases",purchase);
    }

    public void postAccount(Account account){
        execute(ApiClient.this,POST_ACCOUNT,"/accounts",account);
    }

    public void postLogin(String email, String password){
        Account account = new Account(email,password);
        execute(ApiClient.this,POST_ACCOUNT,"/user/login",account);
    }

    public void getAccount(int id){
        execute(ApiClient.this, GET_ACCOUNT, "/accounts/"+id);
    }

    public void getArticleList(int accountId){
        execute(ApiClient.this, GET_MY_ARTICLES, "/user/"+accountId+"/articles");
    }

    public void getArticleList(int accountId, int myId){
        execute(ApiClient.this, GET_MY_LIST, "/user/"+accountId+getMyPath(myId));
    }

    public void getPurchase(int id){
        execute(ApiClient.this, GET_PURCHASE,"/purchases/"+id);
    }

    private static final int PARSE_ARTICLE_LIST = 2;
    private static final int PARSE_ARTICLE = 3;
    private static final int PARSE_ACCOUNT = 5;
    private static final int PARSE_PURCHASE = 7;
    private static final int PARSE_PURCHASE_TO_ARTICLE = 11;

    private static final int GET_ARTICLE_LIST = 13;
    private static final int GET_ARTICLE = 17;
    private static final int POST_PURCHASE = 19;
    private static final int POST_ACCOUNT = 23;
    private static final int GET_ACCOUNT = 29;
    private static final int GET_MY_ARTICLES = 31;
    private static final int GET_MY_LIST = 37;
    private static final int GET_PURCHASE = 41;

    @Override
    public Object parse(Response response, int requestCode) {
        switch (requestCode){
            case PARSE_ARTICLE_LIST:
                Type listType = new TypeToken<List<Article>>(){}.getType();
                return gson.fromJson(response.body().charStream(),listType);

            case PARSE_ARTICLE:
                String json = "";

                try {
                    json = response.body().string();
                } catch (IOException e) {
                    Log.e(TAG,e.toString());
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

            case PARSE_ACCOUNT:
                return gson.fromJson(response.body().charStream(),Account.class);

            case PARSE_PURCHASE:
                return gson.fromJson(response.body().charStream(),Purchase.class);

            case PARSE_PURCHASE_TO_ARTICLE:
                Type typeList = new TypeToken<List<Purchase>>(){}.getType();
                List<Purchase> purchases = gson.fromJson(response.body().charStream(),typeList);
                return extractArticles(purchases);

            default:
                throw new AssertionError("Forgot to implement");
        }
    }

    @Override
    public void httpRequest(int requestCode, Object... args) {
        String path = (String) args[0];
        Object postBody = null;

        if(args.length > 1){
            postBody = args[1];
        }

        switch(requestCode){
            case GET_ARTICLE_LIST:
                httpClient
                        .newCall(makeGetRequest(path))
                        .enqueue(makeCallback(ApiClient.this,PARSE_ARTICLE_LIST));
                break;

            case GET_ARTICLE:
                httpClient
                        .newCall(makeGetRequest(path))
                        .enqueue(makeCallback(ApiClient.this,PARSE_ARTICLE));
                break;

            case POST_PURCHASE:
                httpClient
                        .newCall(makePostRequest(path,postBody))
                        .enqueue(makeCallback(ApiClient.this,PARSE_PURCHASE));
                break;

            case POST_ACCOUNT:
                httpClient
                        .newCall(makePostRequest(path, postBody))
                        .enqueue(makeCallback(ApiClient.this,PARSE_ACCOUNT));
                break;

            case GET_ACCOUNT:
                httpClient
                        .newCall(makeGetRequest(path))
                        .enqueue(makeCallback(ApiClient.this,PARSE_ACCOUNT));
                break;

            case GET_MY_ARTICLES:
                httpClient
                        .newCall(makeGetRequest(path))
                        .enqueue(makeCallback(ApiClient.this,PARSE_ARTICLE_LIST));
                break;

            case GET_MY_LIST:
                httpClient
                        .newCall(makeGetRequest(path))
                        .enqueue(makeCallback(ApiClient.this,PARSE_PURCHASE_TO_ARTICLE));
                break;

            case GET_PURCHASE:
                httpClient
                        .newCall(makeGetRequest(path))
                        .enqueue(makeCallback(ApiClient.this,PARSE_PURCHASE));
                break;

            default:
                throw new AssertionError("Forgot to implement");
        }
    }
}
