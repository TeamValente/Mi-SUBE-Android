package xyz.marianomolina.misube.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import xyz.marianomolina.misube.model.PuntosCarga;

/**
 * Created by hernancoppola on 1/3/16.
 */
public interface DondeCargoAPI {

    @FormUrlEncoded
    @POST("core/?query=getNearPoints")
    Call<PuntosCarga> loadPuntosCarga(@Field("session") String session,
                                      @Field("lat") String lat,
                                      @Field("lng") String lng
                                     );



}
