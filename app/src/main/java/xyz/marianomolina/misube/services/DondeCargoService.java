package xyz.marianomolina.misube.services;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import xyz.marianomolina.misube.model.Filtro;
import xyz.marianomolina.misube.model.PuntoCarga;
import xyz.marianomolina.misube.utils.EstadoNegocio;

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


        Iterator<PuntoCarga> it= listadoDePuntos.iterator();

        while(it.hasNext()) {

            PuntoCarga punto= it.next();
            if (miFiltro.isOcultarCerrados() && punto.estaAbierto() != EstadoNegocio.Abierto ) {

                it.remove();
            }
            if (miFiltro.isOcultarCobraCarga() && punto.cobraPorCargar() ) {
                it.remove();
            }
            if (miFiltro.isOcultarNoVendeSUBE() && punto.vendeSube() == false ) {
                it.remove();
            }
            if (miFiltro.isOcutarHorarioSinIndicar() && punto.estaAbierto() == EstadoNegocio.Indeterminado ) {
                it.remove();
            }

        }
        
        return listadoDePuntos;

    }

}
