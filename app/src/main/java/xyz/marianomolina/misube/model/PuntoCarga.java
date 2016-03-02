package xyz.marianomolina.misube.model;

import java.util.Date;

import xyz.marianomolina.misube.utils.EstadoNegocio;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class PuntoCarga {

    private int idPunto;
    private String address;
    private double latitude;
    private double longitude;
    private String type;
    private int icon;
    private int cost;
    private int hourOpen;
    private int hourClose;
    private int flagSeller;
    private int flagInvalid;
    private int idType;

    public int getIdPunto() {
        return idPunto;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }

    public int getIcon() {
        return icon;
    }

    public int getCost() {
        return cost;
    }

    public int getHourOpen() {
        return hourOpen;
    }

    public int getHourClose() {
        return hourClose;
    }

    public int getFlagSeller() {
        return flagSeller;
    }

    public int getFlagInvalid() {
        return flagInvalid;
    }

    public int getIdType() {
        return idType;
    }

    public PuntoCarga(int idPunto, String address, double latitude, double longitude, String type, int icon, int cost, int hourOpen, int hourClose, int flagSeller, int flagInvalid) {
        this.idPunto = idPunto;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.icon = icon;
        this.cost = cost;
        this.hourOpen = hourOpen;
        this.hourClose = hourClose;
        this.flagSeller = flagSeller;
        this.flagInvalid = flagInvalid;
    }

    public PuntoCarga(int idPunto, String address, double latitude, double longitude, String type, int icon, int cost, int hourOpen, int hourClose, int flagSeller) {
        this.idPunto = idPunto;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.icon = icon;
        this.cost = cost;
        this.hourOpen = hourOpen;
        this.hourClose = hourClose;
        this.flagSeller = flagSeller;
    }

    public PuntoCarga(int idPunto, String address, double latitude, double longitude, String type, int icon, int cost, int hourOpen, int hourClose, int flagSeller, int idType) {
        this.idPunto = idPunto;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.icon = icon;
        this.cost = cost;
        this.idType = idType;
        this.hourOpen = hourOpen;
        this.hourClose = hourClose;
        this.flagSeller = flagSeller;
    }

    public EstadoNegocio estaAbierto() {
        //Get Current Date
        Date currentDate = new Date();

        if (this.hourClose + this.hourOpen == 0) {
            //Sin horario determinado devuelvo false
            return EstadoNegocio.Indeterminado;
        }
        if (currentDate.getTime() >= this.hourOpen && currentDate.getTime() < this.hourClose) {
            return EstadoNegocio.Abierto;
        } else if (this.hourOpen >= this.hourClose) {
            return EstadoNegocio.Abierto;
        } else {
            return EstadoNegocio.Cerrado;
        }

    }

    public String detalleParaMapa() {
        return this.type;
    }

    public String getHorarioDeAtencion() {
        if (this.hourClose + this.hourOpen != 0) {
            String apertura = this.hourOpen + ":00";
            String cierre = this.hourClose + ":00";
            return  apertura + " - " + cierre + " HS";
        } else {
            //En caso de que no tenga horario cargado muestro solo el tipo
            return "Sin horario cargado";
        }
    }

    public boolean vendeSube() {
        return this.flagSeller > 0;
    }

    public boolean cobraPorCargar() {
        return this.cost > 0;

    }

}
