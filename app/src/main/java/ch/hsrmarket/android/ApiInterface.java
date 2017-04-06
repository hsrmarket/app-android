package ch.hsrmarket.android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by athanabalasingam on 05.04.17.
 */

public interface ApiInterface {
    @GET("Books")
    Call< List<Book> > getAllBooks();

    @POST("Books")
    Call<Book> createBook(@Body Book book);
}
