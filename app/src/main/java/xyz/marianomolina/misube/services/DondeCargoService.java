package xyz.marianomolina.misube.services;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.marianomolina.misube.model.Filtro;
import xyz.marianomolina.misube.model.PuntoCarga;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class DondeCargoService {
    private static final String LOG_TAG = DondeCargoService.class.getSimpleName();

    Filtro miFiltro = new Filtro();

    public void setMiFiltro(Filtro miFiltro) {
        this.miFiltro = miFiltro;
    }

    public void obtenerPuntosCargaPOST() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dondecargolasube.com.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DondeCargoAPI service = retrofit.create(DondeCargoAPI.class);

    }

    public List<PuntoCarga> aplicarFiltro(List<PuntoCarga> listadoDePuntos)
    {

        return listadoDePuntos;

    }

}
