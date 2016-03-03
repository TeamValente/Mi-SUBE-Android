package xyz.marianomolina.misube.services;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.marianomolina.misube.model.Filtro;
import xyz.marianomolina.misube.model.PuntoCarga;
import xyz.marianomolina.misube.model.PuntosCarga;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class DondeCargoService {
    private static final String LOG_TAG = DondeCargoService.class.getSimpleName();

    Filtro miFiltro = new Filtro();

    public void obtenerPuntosCargaPOST() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dondecargolasube.com.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DondeCargoAPI service = retrofit.create(DondeCargoAPI.class);

        Call<PuntosCarga> call = service.loadPuntosCarga("1390472","-34.61201","-58.44356");
        Response<PuntosCarga> execute = call.execute();

        Log.d(LOG_TAG, "Response: " + execute);
    }

}
