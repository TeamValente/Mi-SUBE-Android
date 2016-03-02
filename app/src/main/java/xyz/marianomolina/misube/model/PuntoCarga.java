package xyz.marianomolina.misube.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import xyz.marianomolina.misube.utils.EstadoNegocio;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class PuntoCarga {

    private int id;
    private String address;
    private double latitude;
    private double longitude;
    private String type;
    private String icon;
    private int cost;
    private int hopen;
    private int hclose;
    private int flagSeller;
    private int flagInvalid;
    private String distance;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     * The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     * The cost
     */
    public int getCost() {
        return cost;
    }

    /**
     *
     * @param cost
     * The cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     *
     * @return
     * The hopen
     */
    public int getHopen() {
        return hopen;
    }

    /**
     *
     * @param hopen
     * The hopen
     */
    public void setHopen(int hopen) {
        this.hopen = hopen;
    }

    /**
     *
     * @return
     * The hclose
     */
    public int getHclose() {
        return hclose;
    }

    /**
     *
     * @param hclose
     * The hclose
     */
    public void setHclose(int hclose) {
        this.hclose = hclose;
    }

    /**
     *
     * @return
     * The flagSeller
     */
    public int getFlagSeller() {
        return flagSeller;
    }

    /**
     *
     * @param flagSeller
     * The flag_seller
     */
    public void setFlagSeller(int flagSeller) {
        this.flagSeller = flagSeller;
    }

    /**
     *
     * @return
     * The flagInvalid
     */
    public int getFlagInvalid() {
        return flagInvalid;
    }

    /**
     *
     * @param flagInvalid
     * The flag_invalid
     */
    public void setFlagInvalid(int flagInvalid) {
        this.flagInvalid = flagInvalid;
    }

    /**
     *
     * @return
     * The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public EstadoNegocio estaAbierto() {
        //Get Current Date
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance();

        if (this.getHclose() + this.getHopen() == 0) {
            //Sin horario determinado devuelvo false
            return EstadoNegocio.Indeterminado;
        }
        if (calendar.get(Calendar.HOUR_OF_DAY) >= this.getHopen() &&calendar.get(Calendar.HOUR_OF_DAY) < this.getHclose()) {
            return EstadoNegocio.Abierto;
        } else if (this.getHopen() >= this.getHclose()) {
            return EstadoNegocio.Abierto;
        } else {
            return EstadoNegocio.Cerrado;
        }

    }

    public String detalleParaMapa() {
        return this.type;
    }

    public String getHorarioDeAtencion() {
        if (this.getHclose() + this.getHopen() != 0) {
            String apertura = this.getHopen() + ":00";
            String cierre = this.getHclose() + ":00";
            return  apertura + " - " + cierre + " HS";
        } else {
            //En caso de que no tenga horario cargado muestro solo el tipo
            return "Sin horario cargado";
        }
    }

    public boolean vendeSube() {
        return this.getFlagSeller() > 0;
    }

    public boolean cobraPorCargar() {return this.getCost() > 0;}

}
