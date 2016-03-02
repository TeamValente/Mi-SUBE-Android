package xyz.marianomolina.misube.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import xyz.marianomolina.misube.model.PuntoCarga;

/**
 * Created by hernancoppola on 1/3/16.
 */
public interface IDondeCargoService {

    //@POST("/tasks")
    //Call<PuntoCarga> obtenerPuntosPOST(@Body PuntoCarga puntoCarga);


    //Call<List<PuntoCarga>> repoPuntoCarga(
    //            @Path(“session”) String session,
    //            @Path(“lat”) String repo,
    //            @Path(“lng”) String repo);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://dondecargolasube.com.ar/core/?query=getNearPoints")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
