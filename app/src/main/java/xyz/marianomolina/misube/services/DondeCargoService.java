package xyz.marianomolina.misube.services;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.marianomolina.misube.model.Filtro;

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

    }

}
