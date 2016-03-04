package xyz.marianomolina.misube.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class MiUbicacion {
    double latitud;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    double longitude;

    private Location loc; //Ubicacion Actual
    private Location instLoc = new Location("punto1"); // Ubicacion del puntoCarga


    public Distancia getDistanciaAPuntoCarga(PuntoCarga punto)
    {

        double valorMetros;
        LatLng point = new LatLng(punto.getLatitude(), punto.getLongitude());
        instLoc.setLatitude(point.latitude);
        instLoc.setLongitude(point.longitude);

        DecimalFormat df = new DecimalFormat("0.00##");
        loc.setLatitude(this.latitud);
        loc.setLongitude(this.longitude);

        //loc es un obejto de tipo Location que guarda mi posición
        valorMetros = (double)loc.distanceTo(instLoc);

        Distancia retDistancia;
        String unidadRet = "Metro";

        if (valorMetros >= 1000)
        {
            double valorEnKm = valorMetros/1000;

            if (valorEnKm == 1)
            {
                unidadRet = "kilometro";
            }
            else
            {
                unidadRet = "kilometros";
            }
            retDistancia = new Distancia(valorEnKm, df.format(valorEnKm),unidadRet);

        }else
        {
            if (valorMetros == 1)
            {
                unidadRet = "metro";
            }

            retDistancia = new Distancia(valorMetros, df.format(valorMetros),unidadRet);
        }

        return retDistancia;

    }



}
