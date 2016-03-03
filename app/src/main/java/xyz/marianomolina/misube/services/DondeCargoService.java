package xyz.marianomolina.misube.services;

import java.io.IOException;
import java.util.List;

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
        DondeCargoAPI service = DondeCargoAPI.retrofit.create(DondeCargoAPI.class);

    }

    public List<PuntoCarga> aplicarFiltro(List<PuntoCarga> listadoDePuntos) {

        return listadoDePuntos;

    }

}
