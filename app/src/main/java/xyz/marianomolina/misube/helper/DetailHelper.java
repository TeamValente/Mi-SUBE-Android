package xyz.marianomolina.misube.helper;

import xyz.marianomolina.misube.model.Distancia;
import xyz.marianomolina.misube.model.MiUbicacion;
import xyz.marianomolina.misube.model.PuntoCarga;
import xyz.marianomolina.misube.utils.EstadoNegocio;

/**
 * Created by hernancoppola on 4/3/16.
 */
public class DetailHelper {

    PuntoCarga datos;

    public DetailHelper(PuntoCarga datos)
    {
        this.datos = datos;
    }

    public String getDireccion()
    {
        return this.datos.getAddress();
    }

    public String getDistancia(MiUbicacion miUbicacion)
    {
        Distancia distancia = miUbicacion.getDistanciaAPuntoCarga(this.datos);
        return distancia.getValorString() + " " + distancia.getUnidad();
    }

    public String getHorario()
    {
        if (datos.estaAbierto() == EstadoNegocio.Abierto)
        {
            return datos.getHorarioDeAtencion() + ", Abierto ahora";
        }else if (datos.estaAbierto() == EstadoNegocio.Cerrado)
        {
            return datos.getHorarioDeAtencion() + ", Cerrado";
        }else
        {
            return "Sin horario cargado";
        }

    }

    public String getVendeSube()
    {
        if (datos.vendeSube())
        {
            return "Si";
        }else{
            return "No";
        }
    }

    public String getCobraCarga()
    {
        if (datos.cobraPorCargar())
        {
            return "Si";
        }else{return "No";}
    }

    public String getTipoPunto()
    {
        return datos.getType();
    }
}
