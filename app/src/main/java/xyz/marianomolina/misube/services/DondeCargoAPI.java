package xyz.marianomolina.misube.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import xyz.marianomolina.misube.model.PuntoCarga;

/**
 * Created by hernancoppola on 1/3/16.
 */
public interface DondeCargoAPI {

    @FormUrlEncoded
    @POST("core/?query=getNearPoints")
    Call<List<PuntoCarga>> loadPuntosCarga(@Field("session") String session, @Field("params[lat]") double lat, @Field("params[lng]") double lng);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://dondecargolasube.com.ar/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
